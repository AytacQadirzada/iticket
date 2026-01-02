package com.example.iticket.model.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TicketMailResponse {
    private String ticketNumber;
    private String eventName;
    private double price;
    private String venue;
    private String hall;
    private String sector;
    private String rowNumber;
    private String seatNumber;
    private LocalDateTime startDate;
}
