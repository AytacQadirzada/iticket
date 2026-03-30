package com.example.iticket.model.response;

import com.example.iticket.dao.entity.SectorEntity;
import com.example.iticket.dao.entity.UserEntity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class TicketResponse {
    private Long id;
    private double price;
    private String number;
    private Long rowNumber;
    private Long columnNumber;
    private boolean isBooked;
    private String sectorName;
//    private UserEntity user;
}
