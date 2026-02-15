package com.example.iticket.model.request;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class HallUpdateRequestDto {
    public int Id;
    public String Name;
    public List<SectorUpdateRequestDto> Sectors;
}
