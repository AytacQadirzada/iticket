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
public class ProductEventResponse {
    private Long id;
    private String eventName;
    private double minPrice;
    private LocalDateTime eventDate;
    private HallResponse hallResponses;
    private List<TicketResponse> ticket;

}
