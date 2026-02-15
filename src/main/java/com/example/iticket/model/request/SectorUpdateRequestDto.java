package com.example.iticket.model.request;

import com.example.iticket.enums.SectorClassification;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SectorUpdateRequestDto {
    public int Id;
    public String Name;
    public int RowCount;
    public int SeatCount;
    public int Capacity;
    public SectorClassification SectorClassification;
}
