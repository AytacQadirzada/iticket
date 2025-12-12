package com.example.iticket.model.request;

import com.example.iticket.enums.SectorClassification;
import jakarta.persistence.*;
import lombok.*;

import java.util.List;
import java.util.Objects;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SectorRequest {
    private String name;
    private SectorClassification sectorClassification;
    public Long hallId;
}
