package com.example.iticket.model.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class HallResponse {
    private Long id;
    private String name;
    private List<SectorResponse> sectorResponses;
}
