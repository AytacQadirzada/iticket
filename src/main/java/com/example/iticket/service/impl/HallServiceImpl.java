package com.example.iticket.service.impl;

import com.example.iticket.dao.entity.HallEntity;
import com.example.iticket.dao.repository.HallRepository;
import com.example.iticket.dao.repository.VenuesRepository;
import com.example.iticket.exception.NotFoundException;
import com.example.iticket.mapper.HallMapper;
import com.example.iticket.model.request.HallRequest;
import com.example.iticket.model.response.HallResponse;
import com.example.iticket.service.concret.HallService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class HallServiceImpl implements HallService {
    private final HallRepository repository;
    private final VenuesRepository venuesRepository;
    private final HallMapper mapper;

    @Override
    public List<HallResponse> getAll() {
        log.info("ActionLog.getAll.start");
        List<HallEntity> entities = repository.findAll();
        System.out.println(entities);
        var responses = entities.stream().map(mapper::toResponse).toList();
        System.out.println(responses);
        log.info("ActionLog.getAll.end");
        return responses;
    }

    @Override
    public HallResponse getById(Long id) {
        log.info("ActionLog.getById.start id: {} ", id);
        HallEntity entity = repository.findById(id).orElseThrow(() -> {
            log.error("ActionLog.getById.error Hall not found with id: {} ", id);
            return new NotFoundException("Hall not found");
        });
        var response = mapper.toResponse(entity);
        log.info("ActionLog.getById.end id: {} ", id);
        return response;
    }

    @Override
    public void create(HallRequest request) {
        log.info("ActionLog.create.start name: {} ", request.getName());
        var  entity = mapper.toEntity(request);
        entity.setVenue(venuesRepository.findById(request.getVenueId()).orElse(null));
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
    public void update(Long id, HallRequest request) {
        log.info("ActionLog.update.start id: {} ", id);
        var entity = repository.findById(id).orElseThrow(() -> {
            log.error("ActionLog.update.error Hall not found with id: {} ", id);
            return new NotFoundException("Hall not found");
        });
        mapper.mapForUpdate(request, entity);
        entity.setId(id);
        repository.save(entity);
        log.info("ActionLog.update.end id: {} ", id);
    }
}
