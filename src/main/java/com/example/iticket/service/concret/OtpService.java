package com.example.iticket.service.concret;

public interface OtpService {
    void generateOtp(String email);
    boolean verifyOtp(String username, String otp);
}
