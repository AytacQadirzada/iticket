package com.example.iticket.service.concret;

import com.example.iticket.model.request.CardRequest;
import com.example.iticket.model.response.IyzicoPaymentResult;

public interface IyzicoPaymentService {
    IyzicoPaymentResult payForPlan(
            Long userId,
            Long basketId,
            CardRequest request
    );
    void refund(String paymentId);
    IyzicoPaymentResult addBalance(
            Long userId,
            double amount,
            CardRequest request);
}
