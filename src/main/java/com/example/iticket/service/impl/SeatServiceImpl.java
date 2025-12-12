package com.example.iticket.service.impl;

import com.example.iticket.dao.entity.SeatEntity;
import com.example.iticket.dao.repository.SeatRepository;
import com.example.iticket.dao.repository.SectorRepository;
import com.example.iticket.exception.NotFoundException;
import com.example.iticket.mapper.SeatMapper;
import com.example.iticket.model.request.SeatRequest;
import com.example.iticket.model.response.SeatResponse;
import com.example.iticket.service.concret.SeatService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class SeatServiceImpl implements SeatService {
    private final SeatRepository repository;
    private final SectorRepository sectorRepository;
    private final SeatMapper mapper;

    @Override
    public List<SeatResponse> getAll() {
        log.info("ActionLog.getAll.start");
        List<SeatEntity> entities = repository.findAll();
        var responses = entities.stream().map(mapper::toResponse).toList();
        log.info("ActionLog.getAll.end");
        return responses;
    }

    @Override
    public SeatResponse getById(Long id) {
        log.info("ActionLog.getById.start id: {} ", id);
        SeatEntity entity = repository.findById(id).orElseThrow(() -> {
            log.error("ActionLog.getById.error Seat not found with id: {} ", id);
            return new NotFoundException("Seat not found");
        });
        var response = mapper.toResponse(entity);
        log.info("ActionLog.getById.end id: {} ", id);
        return response;
    }

    @Override
    public void create(SeatRequest request) {
        log.info("ActionLog.create.start name: {} ", request.getSeatNumber() + request.getRowNumber());
        var  entity = mapper.toEntity(request);
        entity.setSector(sectorRepository.findById(request.getSectorId()).orElse(null));
        repository.save(entity);
        log.info("ActionLog.create.end name: {} ", entity.getSeatNumber() + entity.getRowNumber());
    }

    @Override
    public void delete(Long id) {
        log.info("ActionLog.delete.start id: {} ", id);
        repository.deleteById(id);
        log.info("ActionLog.delete.end id: {} ", id);

    }

    @Override
    public void update(Long id, SeatRequest request) {
        log.info("ActionLog.update.start id: {} ", id);
        var entity = repository.findById(id).orElseThrow(() -> {
            log.error("ActionLog.update.error Seat not found with id: {} ", id);
            return new NotFoundException("Seat not found");
        });
        mapper.mapForUpdate(request, entity);
        entity.setId(id);
        repository.save(entity);
        log.info("ActionLog.update.end id: {} ", id);
    }
}
