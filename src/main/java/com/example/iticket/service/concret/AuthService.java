package com.example.iticket.service.concret;

import com.example.iticket.model.request.*;
import com.example.iticket.model.response.AuthResponse;
import com.example.iticket.model.response.TicketResponse;
import com.example.iticket.model.response.UserResponse;

import java.util.List;

public interface AuthService {

    void registerUser(RegisterUserRequest request);

    AuthResponse login(String email, String password);

    boolean verifyOtp(String otp);

    void generateOtp();

    void updateUser(UserRequest request);

    void deleteUser();

    UserResponse getUser();

    void resetPassword(ResetPasswordRequest request);

    List<TicketResponse> myTickets();
    }
