package com.example.iticket.model.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserResponse {
    private Long id;
    private String name;
    private String surname;
    private String email;
    private String phone;
    private boolean gender;
    private String country;
    private LocalDate dateOfBirth;
    private boolean isEmailVerified;

}
