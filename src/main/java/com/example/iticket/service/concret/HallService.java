package com.example.iticket.service.concret;


import com.example.iticket.model.request.HallRequest;
import com.example.iticket.model.request.VenuesRequest;
import com.example.iticket.model.response.HallResponse;
import com.example.iticket.model.response.VenuesResponse;

import java.util.List;

public interface HallService {
    List<HallResponse> getAll();
    HallResponse getById(Long id);
    void create(HallRequest request);
    void delete(Long id);
    void update(Long id, HallRequest request);
}
