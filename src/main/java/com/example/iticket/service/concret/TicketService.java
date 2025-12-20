package com.example.iticket.service.concret;


import com.example.iticket.dao.entity.TicketEntity;
import com.example.iticket.model.response.TicketResponse;

import java.util.List;

public interface TicketService {
    List<TicketResponse> getAll();
    TicketResponse getById(Long id);
}
