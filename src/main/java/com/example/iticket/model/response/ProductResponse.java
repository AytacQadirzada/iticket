package com.example.iticket.model.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ProductResponse {
    private Long id;
    private String bannerImage;
    private String posterImage;
    private String title;
    private LocalDateTime startDate;
    private double minPrice;
    private int ageLimit;
    private String about;
    private String language;
    private String description;
    private CategoryResponse category;
    private List<ProductEventResponse> productEvents;
}
