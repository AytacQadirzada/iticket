package com.example.iticket.service.impl;

import com.example.iticket.dao.entity.CategoryEntity;
import com.example.iticket.dao.entity.HallEntity;
import com.example.iticket.dao.entity.VenuesEntity;
import com.example.iticket.dao.repository.CategoryRepository;
import com.example.iticket.dao.repository.VenuesRepository;
import com.example.iticket.exception.NotFoundException;
import com.example.iticket.mapper.HallMapper;
import com.example.iticket.mapper.SectorMapper;
import com.example.iticket.mapper.VenuesMapper;
import com.example.iticket.model.request.HallUpdateRequestDto;
import com.example.iticket.model.request.VenuesRequest;
import com.example.iticket.model.request.VenuesUpdateRequestDto;
import com.example.iticket.model.response.VenuesResponse;
import com.example.iticket.model.response.VenuesResponse;
import com.example.iticket.service.concret.VenuesService;
import jakarta.transaction.Transactional;
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
    private final HallMapper hallMapper;
    private final SectorMapper sectorMapper;

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
        var venuesEntity = venuesMapper.toEntity(venues);
        for(var hall : venuesEntity.getHalls()){
            hall.setVenue(venuesEntity);
            for(var sector : hall.getSectors()){
                sector.setHall(hall);
            }
        }
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
    @Transactional
    public void updateVenues(Long id, VenuesUpdateRequestDto request) {
        log.info("ActionLog.updateVenues.start id: {} ", id);
        VenuesEntity venue = venuesRepository
                .findById(id)
                .orElseThrow(() -> new NotFoundException("Venue Not found"));

        for (HallUpdateRequestDto hallDto : request.getHalls()) {

            HallEntity hallEntity = venue.getHalls()
                    .stream()
                    .filter(h -> h.getId().equals(hallDto.getId()))
                    .findFirst()
                    .orElse(null);
            if (hallEntity == null) {
                HallEntity newHall = hallMapper.toEntity(hallDto);
                newHall.setVenue(venue);

                for (var sector : newHall.getSectors()) {
                    sector.setHall(newHall);
                }

                venue.getHalls().add(newHall);
            }
            else {
                hallEntity.setName(hallDto.getName());
                hallEntity.setVenue(venue);

                for (var sectorDto : hallDto.getSectors()) {
                    var sectorEntity = hallEntity.getSectors()
                            .stream()
                            .filter(s -> s.getId().equals(sectorDto.getId()))
                            .findFirst()
                            .orElse(null);
                    // === NEW SECTOR ===
                    if (sectorEntity == null) {
                        var newSector = sectorMapper.toEntity(sectorDto);
                        newSector.setHall(hallEntity);
                        hallEntity.getSectors().add(newSector);
                    }
                    else {
                        sectorEntity.setName(sectorDto.getName());
                        sectorEntity.setRowNumber(Long.valueOf(sectorDto.getRowCount()));
                        sectorEntity.setColumnNumber(Long.valueOf(sectorDto.getSeatCount()));
                        sectorEntity.setCapacity(Long.valueOf(sectorDto.getCapacity()));
                        sectorEntity.setSectorClassification(sectorDto.getSectorClassification());
                        sectorEntity.setHall(hallEntity);
                    }
                }
            }
        }

        venuesRepository.save(venue);
        log.info("ActionLog.updateVenues.end id: {} ", id);
    }

}
