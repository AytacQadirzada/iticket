package com.example.iticket.service.impl;

import com.example.iticket.dao.entity.TicketEntity;
import com.example.iticket.dao.repository.SeatRepository;
import com.example.iticket.dao.repository.TicketRepository;
import com.example.iticket.exception.NotFoundException;
import com.example.iticket.mapper.TicketMapper;
import com.example.iticket.model.request.TicketRequest;
import com.example.iticket.model.response.TicketResponse;
import com.example.iticket.service.concret.TicketService;
import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.swing.text.html.parser.Entity;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class TicketServiceImpl implements TicketService {
    private final TicketRepository repository;
    private final SeatRepository seatRepository;
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

    @Override
    public void create(TicketRequest request) {
        log.info("ActionLog.create.start name: {} ", request.getNumber());
        var  entity = mapper.toEntity(request);
        entity.setSeat(seatRepository.findById(request.getSeatId()).orElse(null));
        repository.save(entity);
        log.info("ActionLog.create.end name: {} ", entity.getNumber());
    }

    @Override
    public void delete(Long id) {
        log.info("ActionLog.delete.start id: {} ", id);
        repository.deleteById(id);
        log.info("ActionLog.delete.end id: {} ", id);

    }

    @Override
    public void update(Long id, TicketRequest request) {
        log.info("ActionLog.update.start id: {} ", id);
        var entity = repository.findById(id).orElseThrow(() -> {
            log.error("ActionLog.update.error Ticket not found with id: {} ", id);
            return new NotFoundException("Ticket not found");
        });
        mapper.mapForUpdate(request, entity);
        entity.setId(id);
        repository.save(entity);
        log.info("ActionLog.update.end id: {} ", id);
    }

    private final EntityManager entityManager;
    @Override
    @Transactional
    public void bulkInsert(List<TicketEntity> entities) {
        int batchSize = 50;
        for (int i = 0; i < entities.size(); i++) {
            entityManager.persist(entities.get(i));
            if (i % batchSize == 0 && i > 0) {
                entityManager.flush();
                entityManager.clear();
            }
        }
    }
}
