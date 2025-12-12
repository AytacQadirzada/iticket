package com.example.iticket.model.request;

import jakarta.persistence.*;
import lombok.*;

import java.util.List;
import java.util.Objects;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SeatRequest {
    private String rowNumber;
    private String seatNumber;
    private Long sectorId;

}
