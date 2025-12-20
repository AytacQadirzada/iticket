package com.example.iticket.service.impl;

import com.example.iticket.dao.entity.TicketEntity;
import com.example.iticket.dao.repository.TicketRepository;
import com.example.iticket.exception.NotFoundException;
import com.example.iticket.mapper.TicketMapper;
import com.example.iticket.model.response.TicketResponse;
import com.example.iticket.service.concret.TicketService;
import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class TicketServiceImpl implements TicketService {
    private final TicketRepository repository;
    private final TicketMapper mapper;

    @Override
    public List<TicketResponse> getAll() {
        log.info("ActionLog.getAll.start");
        List<TicketEntity> entities = repository.findAll();
        var responses = entities.stream().map(mapper::toResponse).toList();
        log.info("ActionLog.getAll.end");
        return responses;
    }

    @Override
    public TicketResponse getById(Long id) {
        log.info("ActionLog.getById.start id: {} ", id);
        TicketEntity entity = repository.findById(id).orElseThrow(() -> {
            log.error("ActionLog.getById.error Ticket not found with id: {} ", id);
            return new NotFoundException("Ticket not found");
        });
        var response = mapper.toResponse(entity);
        log.info("ActionLog.getById.end id: {} ", id);
        return response;
    }
}
