package com.example.iticket.service.impl;

import com.example.iticket.dao.entity.*;
import com.example.iticket.dao.repository.*;
import com.example.iticket.exception.NotFoundException;
import com.example.iticket.mapper.ProductEventMapper;
import com.example.iticket.mapper.ProductMapper;
import com.example.iticket.model.request.ProductEventRequest;
import com.example.iticket.model.request.ProductRequest;
import com.example.iticket.model.response.ProductResponse;
import com.example.iticket.service.concret.ProductEventService;
import com.example.iticket.service.concret.ProductService;
import com.example.iticket.service.concret.TicketService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class ProductServiceImpl implements ProductService {
    private final ProductRepository repository;
    private final ProductMapper mapper;
    private final SectorRepository sectorRepository;
    private final TicketRepository ticketRepository;
    private final CategoryRepository categoryRepository;
    private final ProductEventMapper productEventMapper;

    @Override
    public List<ProductResponse> getAll() {
        log.info("ActionLog.getAll.start");
        List<ProductEntity> entities = repository.findAll();
        System.out.println(entities);
        var responses = entities.stream().map(mapper::toResponse).toList();
        System.out.println(responses);
        log.info("ActionLog.getAll.end");
        return responses;
    }

    @Override
    public ProductResponse getById(Long id) {
        log.info("ActionLog.getById.start id: {} ", id);
        ProductEntity entity = repository.findById(id).orElseThrow(() -> {
            log.error("ActionLog.getById.error Product not found with id: {} ", id);
            return new NotFoundException("Product not found");
        });
        var response = mapper.toResponse(entity);
        log.info("ActionLog.getById.end id: {} ", id);
        return response;
    }

    @Override
    @Transactional
    public void create(ProductRequest request) {
        log.info("ActionLog.create.start name: {}", request.getTitle());

        var entity = mapper.toEntity(request);
        entity.setProductEvents(new ArrayList<>());

        List<ProductEventEntity> productEvents = new ArrayList<>();

        for (ProductEventRequest productEventRequest : request.getProductEvents()) {
            var hallId = sectorRepository.findById(productEventRequest.getSectorPrices().get(0).getSectorId()).get().getHall().getId();



            var sectors = sectorRepository.findByHallId(hallId);

            var productEventEntity = productEventMapper.toEntity(productEventRequest);
            for (SectorEntity sector : sectors) {
                var sectorPrice = productEventRequest.getSectorPrices().stream()
                        .filter(n -> n.getSectorId() == sector.getId())
                        .findFirst()
                        .orElseThrow()
                        .getPrice();
                if(sector.getRowNumber() !=0 && sector.getColumnNumber() !=0) {
                        for (int i = 0; i < sector.getColumnNumber(); i++) {
                        for (int j=0; j<sector.getRowNumber(); j++) {
                            var ticket = new TicketEntity();
                            ticket.setPrice(sectorPrice);
                            ticket.setColumnNumber((long) (i + 1));
                            ticket.setRowNumber((long) (j + 1));
                            ticket.setNumber(UUID.randomUUID().toString());
                            ticket.setProductEvent(productEventEntity);
                            ticketRepository.save(ticket);
                        }
                    }
                } else if (sector.getRowNumber() == 0 && sector.getColumnNumber() == 0 && sector.getCapacity() != 0){ {
                    for (int i = 0; i < sector.getCapacity(); i++) {
                        var ticket = new TicketEntity();
                        ticket.setPrice(sectorPrice);
                        ticket.setNumber(UUID.randomUUID().toString());
                        ticket.setProductEvent(productEventEntity);
                        ticketRepository.save(ticket);
                    }
                }

                }

//                for (SeatEntity seat : seats) {
//                    var ticket = new TicketEntity();
//                    ticket.setPrice(sectorPrice);
////                    ticket.setSeat(seat);
//                    ticket.setNumber(UUID.randomUUID().toString());
//                    ticket.setProductEvent(productEventEntity);
//                    ticketRepository.save(ticket);
//                }
            }


//            productEventEntity.setHall(hall);
            productEventEntity.setProduct(entity);


            productEvents.add(productEventEntity);
        }

        entity.setCategory(categoryRepository.findById(request.getCategoryId()).orElse(null));

        entity.setProductEvents(productEvents);

        repository.save(entity);

        log.info("ActionLog.create.end name: {}", request.getTitle());
    }

    @Override
    public void delete(Long id) {
        log.info("ActionLog.delete.start id: {} ", id);
        repository.deleteById(id);
        log.info("ActionLog.delete.end id: {} ", id);
    }

    @Override
    public void update(Long id, ProductRequest request) {
        log.info("ActionLog.update.start id: {} ", id);
        var entity = repository.findById(id).orElseThrow(() -> {
            log.error("ActionLog.update.error Product not found with id: {} ", id);
            return new NotFoundException("Product not found");
        });
        mapper.mapForUpdate(request, entity);
        entity.setId(id);
        repository.save(entity);
        log.info("ActionLog.update.end id: {} ", id);
    }

    @Override
    public List<ProductResponse> getAllByCategory(Long categoryId) {
        log.info("ActionLog.getAllByCategory.start categoryId: {} ", categoryId);
        List<ProductEntity> entities = repository.getAllByCategoryId(categoryId);
        var responses = entities.stream().map(mapper::toResponse).toList();
        log.info("ActionLog.getAllByCategory.end categoryId: {} ", categoryId);
        return responses;
    }
}
