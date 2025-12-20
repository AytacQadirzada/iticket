package com.example.iticket.service.concret;


import com.example.iticket.model.request.SectorRequest;
import com.example.iticket.model.response.SectorResponse;

import java.util.List;

public interface SectorService {
    List<SectorResponse> getAll();
    SectorResponse getById(Long id);
    void create(SectorRequest request);
    void delete(Long id);
    void update(Long id, SectorRequest request);
}
