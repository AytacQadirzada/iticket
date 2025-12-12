package com.example.iticket.model.response;

import com.example.iticket.enums.SectorClassification;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SectorResponse {
    private Long id;
    private String name;
    private SectorClassification sectorClassification;
    public List<SeatResponse> seatResponses;
}
