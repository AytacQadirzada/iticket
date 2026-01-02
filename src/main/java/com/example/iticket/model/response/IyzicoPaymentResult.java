package com.example.iticket.model.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class IyzicoPaymentResult {
    private boolean success;
    private String paymentId;
    public String errorCode;
    public String errorMessage;
}
