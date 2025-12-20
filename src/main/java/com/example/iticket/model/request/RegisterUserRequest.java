package com.example.iticket.model.request;

import com.example.iticket.enums.Role;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;


@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class RegisterUserRequest {

    private String name;
    private String surname;
    private String email;
    private String password;
    private String phone;
    private boolean gender;
    private Set<String> role;
}
