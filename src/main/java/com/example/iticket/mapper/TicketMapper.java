package com.example.iticket.mapper;

import com.example.iticket.dao.entity.TicketEntity;
import com.example.iticket.model.request.TicketRequest;
import com.example.iticket.model.response.TicketResponse;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

@Mapper(componentModel = "spring", nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE )
public interface TicketMapper {
    TicketEntity toEntity(TicketRequest request);
    TicketResponse toResponse(TicketEntity entity);
    void mapForUpdate(TicketRequest request,@MappingTarget TicketEntity entity);

}
