package com.example.iticket.controller;

import com.example.iticket.model.request.RegisterUserRequest;
import com.example.iticket.model.request.UserRequest;
import com.example.iticket.model.response.AuthResponse;
import com.example.iticket.model.response.UserResponse;
import com.example.iticket.service.concret.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/v1/user")
@RequiredArgsConstructor
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
}
