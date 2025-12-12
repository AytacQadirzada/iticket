package com.example.iticket.model.request;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProductEventRequest {
    private String eventName;
    private double minPrice;
    private LocalDateTime eventDate;
    private Long hallId;
//    private Long productId;
    private List<SectorPriceRequest> sectorPrices;

}
