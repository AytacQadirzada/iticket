package com.example.iticket.service.concret;


import com.example.iticket.model.request.ProductRequest;
import com.example.iticket.model.request.SeatRequest;
import com.example.iticket.model.response.ProductResponse;
import com.example.iticket.model.response.SeatResponse;

import java.util.List;

public interface SeatService {
    List<SeatResponse> getAll();
    SeatResponse getById(Long id);
    void create(SeatRequest request);
    void delete(Long id);
    void update(Long id, SeatRequest request);
}
