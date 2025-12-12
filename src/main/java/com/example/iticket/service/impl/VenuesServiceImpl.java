package com.example.iticket.service.impl;

import com.example.iticket.dao.entity.CategoryEntity;
import com.example.iticket.dao.entity.VenuesEntity;
import com.example.iticket.dao.repository.CategoryRepository;
import com.example.iticket.dao.repository.VenuesRepository;
import com.example.iticket.exception.NotFoundException;
import com.example.iticket.mapper.VenuesMapper;
import com.example.iticket.model.request.VenuesRequest;
import com.example.iticket.model.response.VenuesResponse;
import com.example.iticket.model.response.VenuesResponse;
import com.example.iticket.service.concret.VenuesService;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class VenuesServiceImpl implements VenuesService {

    private final VenuesRepository venuesRepository;
    private final VenuesMapper venuesMapper;
    @Override
    public List<VenuesResponse> getAll() {
        log.info("ActionLog.getAll.start");
        List<VenuesEntity> venuesEntities = venuesRepository.findAll();
        var categories = venuesEntities.stream().map(venuesMapper::toResponse).toList();
        log.info("ActionLog.getAll.end");
        return categories;
    }

    @Override
    public VenuesResponse getById(Long id) {
        log.info("ActionLog.getById.start id: {} ", id);
        VenuesEntity venuesEntity = venuesRepository.findById(id).orElseThrow(() -> {
            log.error("ActionLog.getById.error Venues not found with id: {} ", id);
            return new NotFoundException("Venues not found");
        });
        var venuesResponse = venuesMapper.toResponse(venuesEntity);
        log.info("ActionLog.getById.end id: {} ", id);
        return venuesResponse;
    }

    @Override
    public void createVenues(VenuesRequest venues) {
        log.info("ActionLog.createVenues.start name: {} ", venues.getName());
        var  venuesEntity = venuesMapper.toEntity(venues);
        venuesRepository.save(venuesEntity);
        log.info("ActionLog.createVenues.end name: {} ", venuesEntity.getName());
    }

    @Override
    public void deleteVenues(Long id) {
        log.info("ActionLog.deleteVenues.start id: {} ", id);
        venuesRepository.deleteById(id);
        log.info("ActionLog.deleteVenues.end id: {} ", id);

    }

    @Override
    public void updateVenues(Long id, VenuesRequest venues) {
        log.info("ActionLog.updateVenues.start id: {} ", id);
        var entity = venuesRepository.findById(id).orElseThrow(() -> {
            log.error("ActionLog.updateVenues.error Venues not found with id: {} ", id);
            return new NotFoundException("Venues not found");
        });
        venuesMapper.mapForUpdate(venues, entity);
        entity.setId(id);
        venuesRepository.save(entity);
        log.info("ActionLog.updateVenues.end id: {} ", id);
    }
}
