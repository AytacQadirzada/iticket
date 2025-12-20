package com.example.iticket.controller;

import com.example.iticket.model.request.RegisterUserRequest;
import com.example.iticket.model.request.ResetPasswordRequest;
import com.example.iticket.model.request.UserRequest;
import com.example.iticket.model.response.AuthResponse;
import com.example.iticket.model.response.UserResponse;
import com.example.iticket.service.concret.AuthService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/v1/user")
@RequiredArgsConstructor
@SecurityRequirement(name = "bearerAuth")
public class UserController {
    private final AuthService authService;

    @GetMapping
    public UserResponse profile(@RequestParam String email){
        return authService.getUser(email);
    }



    @PutMapping("/{id}")
    public void updateUser(@PathVariable Long id, @RequestBody UserRequest request){
        authService.updateUser(id,request);
    }

    @DeleteMapping("/{id}")
    public void deleteUser(@PathVariable Long id){
        authService.deleteUser(id);
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
