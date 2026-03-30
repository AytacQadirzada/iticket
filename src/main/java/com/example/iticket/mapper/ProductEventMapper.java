package com.example.iticket.mapper;

import com.example.iticket.dao.entity.HallEntity;
import com.example.iticket.dao.entity.ProductEventEntity;
import com.example.iticket.dao.entity.TicketEntity;
import com.example.iticket.model.request.ProductEventRequest;
import com.example.iticket.model.response.ProductEventResponse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.Named;
import org.mapstruct.NullValuePropertyMappingStrategy;

import java.util.List;

@Mapper(componentModel = "spring", uses = {HallMapper.class, TicketMapper.class}, nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface ProductEventMapper {
    ProductEventEntity toEntity(ProductEventRequest request);

    @Mapping(source = "tickets", target = "hallResponses", qualifiedByName = "extractHallFromTickets")
    @Mapping(source = "tickets", target = "ticket")
    ProductEventResponse toResponse(ProductEventEntity entity);
    List<ProductEventResponse> toListResponse(List<ProductEventEntity> entities);

    void mapForUpdate(ProductEventRequest request, @MappingTarget ProductEventEntity entity);

    @Named("extractHallFromTickets")
    default HallEntity extractHallFromTickets(List<TicketEntity> tickets) {
        if (tickets == null || tickets.isEmpty()) {
            return null;
        }
        TicketEntity firstTicket = tickets.get(0);
        if (firstTicket == null || firstTicket.getSector() == null) {
            return null;
        }
        return firstTicket.getSector().getHall();
    }

}
