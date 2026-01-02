package com.example.iticket.model.request;

import lombok.*;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class HallRequest {
    private String name;
    private List<SectorRequest> sectors;
}
