package com.example.iticket.model.request;

import jakarta.persistence.*;
import lombok.*;

import java.util.Objects;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TicketRequest {
    private double price;
    private String number;
    private Long seatId;
}
