package com.example.iticket.controller;

import com.example.iticket.model.request.RegisterUserRequest;
import com.example.iticket.model.request.ResetPasswordRequest;
import com.example.iticket.model.response.AuthResponse;
import com.example.iticket.model.response.UserResponse;
import com.example.iticket.service.concret.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/v1/auth")
@RequiredArgsConstructor
public class AuthController {
    private final AuthService authService;

    @PostMapping("/register")
    @ResponseStatus(HttpStatus.CREATED)
    public void registerUser(@RequestBody RegisterUserRequest request) {
        authService.registerUser(request);
    }

    @PostMapping("/login")
    public AuthResponse login(@RequestParam String email, @RequestParam String password) {
        return authService.login(email, password);
    }

    @PostMapping("/otp")
    @PreAuthorize("hasRole('ADMIN')")
    public void otp(@RequestParam String email) {
        authService.generateOtp(email);
    }

    @PostMapping("/otp/verify")
    public boolean verifyOtp(@RequestParam String email, @RequestParam String otp) {
        return authService.verifyOtp(email, otp);
    }

    @PostMapping("/forgot-password/otp")
    public void sendForgotPasswordOtp(@RequestParam String email){
        authService.sendForgotPasswordOtp(email);
    }


    @PutMapping("/forgot-password/reset")
    public void resetPassword(@RequestBody ResetPasswordRequest request){
        authService.resetPassword(request);
    }
}
