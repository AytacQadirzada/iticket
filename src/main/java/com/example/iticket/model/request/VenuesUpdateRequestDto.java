package com.example.iticket.model.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class VenuesUpdateRequestDto {
    public String Address;
    public String Mobile;
    public String Name;
    public String Phone;
    public String MapLat;
    public String MapLng;
    public List<HallUpdateRequestDto> Halls;
}
