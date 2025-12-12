package com.example.iticket.model.response;

import com.example.iticket.enums.Role;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class VenuesResponse {
    private Long id;
    private String address;
    private String mobile;
    private String name;
    private String phone;
    private String mapLat;
    private String mapLng;
    private List<HallResponse> hallResponses;
}
