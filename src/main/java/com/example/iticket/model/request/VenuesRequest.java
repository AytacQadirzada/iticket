package com.example.iticket.model.request;

import jakarta.persistence.Column;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class VenuesRequest {
    private String address;
    private String mobile;
    private String name;
    private String phone;
    private String mapLat;
    private String mapLng;
    private List<HallRequest> halls;
}
