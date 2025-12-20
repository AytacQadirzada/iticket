package com.example.iticket.model.response;

import com.example.iticket.enums.SectorClassification;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class SectorResponse {
    private Long id;
    private String name;
    private SectorClassification sectorClassification;
}
