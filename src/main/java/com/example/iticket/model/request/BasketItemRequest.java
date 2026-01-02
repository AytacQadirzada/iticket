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
    private Long userId;
    private Long productEventId;
    private Integer quantity;
    private Long sectorId;
    private String ticketNumber;
}
