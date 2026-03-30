package com.example.iticket.model.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserRequest {
    @NotBlank(message = "Ad boş ola bilməz")
    @Size(min = 2, max = 50, message = "Ad 2-50 simvol arası olmalıdır")
    private String name;

    @NotBlank(message = "Soyad boş ola bilməz")
    @Size(min = 2, max = 50, message = "Soyad 2-50 simvol arası olmalıdır")
    private String surname;

    @NotBlank(message = "Email boş ola bilməz")
    @Email(message = "Email düzgün formatda deyil")
    private String email;

    @NotBlank(message = "Telefon boş ola bilməz")
    @Pattern(
            regexp = "^(\\+994|0)(50|51|55|70|77|99)\\d{7}$",
            message = "Telefon nömrəsi düzgün formatda deyil"
    )
    private String phone;

    @NotNull(message = "Cins seçilməlidir")
    private Boolean gender;
    private String country;
    private LocalDate dateOfBirth;
}
