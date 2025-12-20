package com.example.iticket.model.request;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ProductRequest {
    private String bannerImage;
    private String posterImage;
    private String title;
    private LocalDateTime startDate;
    private double minPrice;
    private int ageLimit;
    private String about;
    private String language;
    private String description;
    private Long categoryId;
    private List<ProductEventRequest> productEvents;
}
