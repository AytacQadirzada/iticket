package com.example.iticket.service.concret;

import com.example.iticket.model.request.CardRequest;
import com.example.iticket.model.request.RegisterUserRequest;
import com.example.iticket.model.request.ResetPasswordRequest;
import com.example.iticket.model.request.UserRequest;
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

//    void userBalanceIncrease(Double amount, CardRequest request);

    List<TicketResponse> myTickets();
    }
