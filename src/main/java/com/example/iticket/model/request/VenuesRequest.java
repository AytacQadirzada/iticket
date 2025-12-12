package com.example.iticket.model.request;

import jakarta.persistence.Column;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class VenuesRequest {
    private String address;
    private String mobile;
    private String name;
    private String phone;
    private String mapLat;
    private String mapLng;
}
