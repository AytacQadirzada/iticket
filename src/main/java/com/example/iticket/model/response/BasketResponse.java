package com.example.iticket.model.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class BasketResponse {
    private List<BasketItemResponse> basketItems;
    private Double totalPrice;
    private Integer totalQuantity;
}
