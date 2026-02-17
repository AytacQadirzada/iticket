package com.example.iticket.model.request;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class RegisterUserRequest {

    @NotBlank(message = "Ad boş ola bilməz")
    @Size(min = 2, max = 50, message = "Ad 2-50 simvol arası olmalıdır")
    private String name;

    @NotBlank(message = "Soyad boş ola bilməz")
    @Size(min = 2, max = 50, message = "Soyad 2-50 simvol arası olmalıdır")
    private String surname;

    @NotBlank(message = "Email boş ola bilməz")
    @Email(message = "Email düzgün formatda deyil")
    private String email;

    @NotBlank(message = "Şifrə boş ola bilməz")
    @Size(min = 8, message = "Şifrə minimum 8 simvol olmalıdır")
    @Pattern(
            regexp = "^(?=.*[A-Z])(?=.*[a-z])(?=.*\\d).*$",
            message = "Şifrə ən az 1 böyük hərf, 1 kiçik hərf və 1 rəqəm içerməlidir"
    )
    private String password;

    @NotBlank(message = "Telefon boş ola bilməz")
    @Pattern(
            regexp = "^(\\+994|0)(50|51|55|70|77|99)\\d{7}$",
            message = "Telefon nömrəsi düzgün formatda deyil"
    )
    private String phone;

    @NotNull(message = "Gender seçilməlidir")
    private Boolean gender;
}
