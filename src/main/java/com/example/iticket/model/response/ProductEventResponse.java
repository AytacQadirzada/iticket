package com.example.iticket.model.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProductEventResponse {
    private Long id;
    private String eventName;
    private double minPrice;
    private LocalDateTime eventDate;

}
