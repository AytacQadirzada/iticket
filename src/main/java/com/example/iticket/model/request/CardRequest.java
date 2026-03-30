package com.example.iticket.model.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CardRequest {
    @NotBlank(message = "Kart sahibi adı boş ola bilməz")
    private String cardHolderName;

    @NotBlank(message = "Kart nömrəsi boş ola bilməz")
    @Pattern(regexp = "^\\d{16}$", message = "Kart nömrəsi 16 rəqəm olmalıdır")
    private String cardNumber;

    @NotBlank(message = "Kartın bitmə ayı boş ola bilməz")
    @Pattern(regexp = "^(0[1-9]|1[0-2])$", message = "Kartın bitmə ayı 01-12 aralığında olmalıdır")
    private String expireMonth;

    @NotBlank(message = "Kartın bitmə ili boş ola bilməz")
    @Pattern(regexp = "^\\d{2}$", message = "Kartın bitmə ili 2 rəqəm olmalıdır")
    private String expireYear;

    @NotBlank(message = "CVV boş ola bilməz")
    @Pattern(regexp = "^\\d{3}$", message = "CVV 3 rəqəm olmalıdır")
    private String cvv;
}
