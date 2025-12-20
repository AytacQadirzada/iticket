package com.example.iticket.service.impl;

import com.example.iticket.dao.entity.HallEntity;
import com.example.iticket.dao.entity.ProductEventEntity;
import com.example.iticket.dao.repository.HallRepository;
import com.example.iticket.dao.repository.ProductEventRepository;
import com.example.iticket.dao.repository.ProductRepository;
import com.example.iticket.exception.NotFoundException;
import com.example.iticket.mapper.HallMapper;
import com.example.iticket.mapper.ProductEventMapper;
import com.example.iticket.model.request.HallRequest;
import com.example.iticket.model.request.ProductEventRequest;
import com.example.iticket.model.response.HallResponse;
import com.example.iticket.model.response.ProductEventResponse;
import com.example.iticket.service.concret.HallService;
import com.example.iticket.service.concret.ProductEventService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class ProductEventServiceImpl implements ProductEventService {
    private final ProductEventRepository repository;
    private final HallRepository hallRepository;
    private final ProductRepository productRepository;
    private final ProductEventMapper mapper;

    @Override
    public List<ProductEventResponse> getAll() {
        log.info("ActionLog.getAll.start");
        List<ProductEventEntity> entities = repository.findAll();
        var responses = entities.stream().map(mapper::toResponse).toList();
        log.info("ActionLog.getAll.end");
        return responses;
    }

    @Override
    public ProductEventResponse getById(Long id) {
        log.info("ActionLog.getById.start id: {} ", id);
        ProductEventEntity entity = repository.findById(id).orElseThrow(() -> {
            log.error("ActionLog.getById.error Product Event not found with id: {} ", id);
            return new NotFoundException("Product Event not found");
        });
        var response = mapper.toResponse(entity);
        log.info("ActionLog.getById.end id: {} ", id);
        return response;
    }

    @Override
    public void create(ProductEventRequest request) {
//        log.info("ActionLog.create.start name: {} ", request.getEventName());
//        var  entity = mapper.toEntity(request);
//        entity.setHall(hallRepository.findById(request.getHallId()).orElse(null));
////        entity.setProduct(productRepository.findById(request.getProductId()).orElse(null));
//        repository.save(entity);
//        log.info("ActionLog.create.end name: {} ", entity.getEventName());
    }

    @Override
    public void delete(Long id) {
        log.info("ActionLog.delete.start id: {} ", id);
        repository.deleteById(id);
        log.info("ActionLog.delete.end id: {} ", id);

    }

    @Override
    public void update(Long id, ProductEventRequest request) {
        log.info("ActionLog.update.start id: {} ", id);
        var entity = repository.findById(id).orElseThrow(() -> {
            log.error("ActionLog.update.error Product Event not found with id: {} ", id);
            return new NotFoundException("Product Event not found");
        });
        mapper.mapForUpdate(request, entity);
        entity.setId(id);
        repository.save(entity);
        log.info("ActionLog.update.end id: {} ", id);
    }
}
