package com.example.iticket.service.concret;

import com.example.iticket.model.request.RegisterUserRequest;
import com.example.iticket.model.request.ResetPasswordRequest;
import com.example.iticket.model.request.UserRequest;
import com.example.iticket.model.response.AuthResponse;
import com.example.iticket.model.response.UserResponse;

public interface AuthService {

    void registerUser(RegisterUserRequest request);

    AuthResponse login(String email, String password);

    boolean verifyOtp(String email, String otp);

    void generateOtp(String email);

    void updateUser(Long id, UserRequest request);

    void deleteUser(Long id);
    UserResponse getUser(String email);
    void sendForgotPasswordOtp(String email);


    void resetPassword(ResetPasswordRequest request);
}
