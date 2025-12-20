package com.example.iticket.model.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class BasketItemRequest {
    private Long productId;
    private Integer quantity;
    private String ticketNumber;
}
