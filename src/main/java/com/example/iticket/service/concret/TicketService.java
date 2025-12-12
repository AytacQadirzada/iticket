package com.example.iticket.service.concret;


import com.example.iticket.dao.entity.TicketEntity;
import com.example.iticket.model.request.SectorRequest;
import com.example.iticket.model.request.TicketRequest;
import com.example.iticket.model.response.SectorResponse;
import com.example.iticket.model.response.TicketResponse;

import java.util.List;

public interface TicketService {
    List<TicketResponse> getAll();
    TicketResponse getById(Long id);
    void create(TicketRequest request);
    void delete(Long id);
    void update(Long id, TicketRequest request);
    void bulkInsert(List<TicketEntity> entity);
}
