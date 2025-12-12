package com.example.iticket.service.impl;

import com.example.iticket.dao.entity.SectorEntity;
import com.example.iticket.dao.repository.HallRepository;
import com.example.iticket.dao.repository.SectorRepository;
import com.example.iticket.exception.NotFoundException;
import com.example.iticket.mapper.SectorMapper;
import com.example.iticket.model.request.SectorRequest;
import com.example.iticket.model.response.SectorResponse;
import com.example.iticket.service.concret.SectorService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class SectorServiceImpl implements SectorService {
    private final SectorRepository repository;
    private final HallRepository hallRepository;
    private final SectorMapper mapper;

    @Override
    public List<SectorResponse> getAll() {
        log.info("ActionLog.getAll.start");
        List<SectorEntity> entities = repository.findAll();
        var responses = entities.stream().map(mapper::toResponse).toList();
        log.info("ActionLog.getAll.end");
        return responses;
    }

    @Override
    public SectorResponse getById(Long id) {
        log.info("ActionLog.getById.start id: {} ", id);
        SectorEntity entity = repository.findById(id).orElseThrow(() -> {
            log.error("ActionLog.getById.error Sector not found with id: {} ", id);
            return new NotFoundException("Sector not found");
        });
        var response = mapper.toResponse(entity);
        log.info("ActionLog.getById.end id: {} ", id);
        return response;
    }

    @Override
    public void create(SectorRequest request) {
        log.info("ActionLog.create.start name: {} ", request.getName());
        var  entity = mapper.toEntity(request);
        entity.setHall(hallRepository.findById(request.getHallId()).orElse(null));
        repository.save(entity);
        log.info("ActionLog.create.end name: {} ", entity.getName());
    }

    @Override
    public void delete(Long id) {
        log.info("ActionLog.delete.start id: {} ", id);
        repository.deleteById(id);
        log.info("ActionLog.delete.end id: {} ", id);

    }

    @Override
    public void update(Long id, SectorRequest request) {
        log.info("ActionLog.update.start id: {} ", id);
        var entity = repository.findById(id).orElseThrow(() -> {
            log.error("ActionLog.update.error Sector not found with id: {} ", id);
            return new NotFoundException("Sector not found");
        });
        mapper.mapForUpdate(request, entity);
        entity.setId(id);
        repository.save(entity);
        log.info("ActionLog.update.end id: {} ", id);
    }
}
