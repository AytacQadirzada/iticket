package com.example.iticket.service.impl;

import com.example.iticket.dao.repository.UserRepository;
import com.example.iticket.enums.Role;
import com.example.iticket.exception.NotFoundException;
import com.example.iticket.exception.NotMatchException;
import com.example.iticket.exception.OtpException;
import com.example.iticket.exception.RegistrationException;
import com.example.iticket.mapper.UserMapper;
import com.example.iticket.model.request.RegisterUserRequest;
import com.example.iticket.model.request.ResetPasswordRequest;
import com.example.iticket.model.request.UserRequest;
import com.example.iticket.model.response.AuthResponse;
import com.example.iticket.model.response.UserResponse;
import com.example.iticket.service.concret.AuthService;
import com.example.iticket.service.concret.OtpService;
import com.example.iticket.utils.JwtUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Set;

@Service
@RequiredArgsConstructor
@Slf4j
public class AuthServiceImpl implements AuthService {
    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final PasswordEncoder passwordEncoder;
    private final OtpService otpService;
    private final JwtUtil jwtUtil;

    @Override
    public void registerUser(RegisterUserRequest request) {
        log.info("ActionLog.registerUser.start: email={}", request.getEmail());
        userRepository.findByEmail(request.getEmail()).ifPresent(user -> {
            throw new RegistrationException("email already exists");
        });
        request.setPassword(passwordEncoder.encode(request.getPassword()));
        var user = userMapper.toEntity(request);
        user.setEmailVerified(false);
        user.setRoles(Set.of(Role.ROLE_USER.name()));
        userRepository.save(user);

        log.info("ActionLog.registerUser.end: email={}", request.getEmail());
    }

    @Override
    public AuthResponse login(String email, String password) {
        log.info("ActionLog.login.start: email: {}", email);
        var user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RegistrationException("Invalid email or password"));
        if (!passwordEncoder.matches(password, user.getPassword())) {
            throw new RegistrationException("Invalid email or password");
        }

        var accessToken = jwtUtil.generateAccessToken(user, email);
        var refreshToken = jwtUtil.generateRefreshToken(user, email);
        var response = new AuthResponse(user.getId(),accessToken, refreshToken, user.getRoles());
        log.info("ActionLog.login.end: email: {}", email);
        return response;
    }



    @Override
    public boolean verifyOtp(String email, String otp) {
        log.info("ActionLog.verifyOtp.start: email: {}", email);
        boolean verify = otpService.verifyOtp(email, otp);
        var user = userRepository.findByEmail(email).orElseThrow(() -> new NotFoundException("User not found"));
        if (verify) {
            user.setEmailVerified(true);
        }
        userRepository.save(user);
        log.info("ActionLog.verifyOtp.end: email: {}", email);
        return verify;
    }

    @Override
    public void generateOtp(String email) {
        log.info("ActionLog.generateOtp.start: email: {}", email);
        otpService.generateOtp(email);
        log.info("ActionLog.generateOtp.end: email: {}", email);
    }

    @Override
    public void updateUser(Long id, UserRequest request){
        log.info("ActionLog.updateUser.start id: {} ", id);
        var entity = userRepository.findById(id).orElseThrow(() -> {
            log.error("ActionLog.updateUser.error Category not found with id: {} ", id);
            return new NotFoundException("Category not found");
        });
        userMapper.mapForUpdate(request, entity);
        entity.setId(id);
        userRepository.save(entity);
        log.info("ActionLog.updateUser.end id: {} ", id);
    }

    @Override
    public void deleteUser(Long id){
        log.info("ActionLog.deleteUser.start id: {} ", id);
        userRepository.deleteById(id);
        log.info("ActionLog.deleteUser.end id: {} ", id);
    }

    @Override
    public UserResponse getUser(String email){
        log.info("ActionLog.getUser.start email: {} ", email);
        var user = userRepository.findByEmail(email).orElseThrow(() -> {
            log.error("ActionLog.getUser.error User not found with email: {} ", email);
            return new NotFoundException("User not found");
        });
        var userResponse = userMapper.toUserResponse(user);
        log.info("ActionLog.getUser.end email: {} ", email);
        return userResponse;
    }

    @Override
    public void sendForgotPasswordOtp(String email) {
        log.info("ActionLog.forgerPassword.start email: {} ", email);
        userRepository.findByEmail(email).orElseThrow(() -> {
            log.error("ActionLog.forgerPassword.error User not found with email: {} ", email);
            return new NotFoundException("User not found");
        });
        otpService.generateOtp(email);
        log.info("ActionLog.forgerPassword.end email: {} ", email);
    }


    @Override
    public void resetPassword(ResetPasswordRequest request) {
        log.info("ActionLog.resetPassword.start email: {} ", request.getEmail());
        var user = userRepository.findByEmail(request.getEmail()).orElseThrow(() -> {
            log.error("ActionLog.resetPassword.error User not found with email: {} ", request.getEmail());
            return new NotFoundException("User not found");
        });
        if(!otpService.verifyOtp(request.getEmail(),request.getOtp())){
            throw new OtpException("Invalid OTP");
        }

        if (!request.getNewPassword().equals(request.getPasswordConfirmation())) {
            log.error("ActionLog.resetPassword.error password not equals: {} ", request.getEmail());
            throw new NotMatchException("Passwords does not match");
        }
        user.setPassword(passwordEncoder.encode(request.getNewPassword()));
        userRepository.save(user);
        log.info("ActionLog.resetPassword.end email: {} ", request.getEmail());

    }
}
