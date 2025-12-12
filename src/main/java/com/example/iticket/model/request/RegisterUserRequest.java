package com.example.iticket.model.request;

import com.example.iticket.enums.Role;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;


@Data
@NoArgsConstructor
@AllArgsConstructor
public class RegisterUserRequest {

    private String name;
    private String surname;
    private String email;
    private String password;
    private String phone;
    private boolean gender;
    private Set<String> role;
}
