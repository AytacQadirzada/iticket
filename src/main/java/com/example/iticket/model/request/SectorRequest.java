package com.example.iticket.model.request;

import com.example.iticket.enums.SectorClassification;
import jakarta.persistence.*;
import lombok.*;

import java.util.List;
import java.util.Objects;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class SectorRequest {
    private String name;
    private SectorClassification sectorClassification;
    private Long hallId;
    private Long rowNumber;
    private Long columnNumber;
    private Long capacity;
}
