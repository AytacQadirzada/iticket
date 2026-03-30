package com.example.iticket.model.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ResetPasswordRequest {

        @NotBlank(message = "Email boş ola bilməz")
        @Email(message = "Email düzgün formatda deyil")
        private String email;

        @NotBlank(message = "Şifrə boş ola bilməz")
        @Size(min = 8, message = "Şifrə minimum 8 simvol olmalıdır")
        @Pattern(
                regexp = "^(?=.*[A-Z])(?=.*[a-z])(?=.*\\d).*$",
                message = "Şifrədə ən az 1 böyük hərf, 1 kiçik hərf və 1 rəqəm olmalıdır"
        )
        private String newPassword;

        @NotBlank(message = "Şifrə boş ola bilməz")
        @Size(min = 8, message = "Şifrə minimum 8 simvol olmalıdır")
        @Pattern(
                regexp = "^(?=.*[A-Z])(?=.*[a-z])(?=.*\\d).*$",
                message = "Şifrədə ən az 1 böyük hərf, 1 kiçik hərf və 1 rəqəm olmalıdır"
        )
        private String passwordConfirmation;
}
