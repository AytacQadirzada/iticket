package com.example.iticket.mapper;

import com.example.iticket.dao.entity.TicketEntity;
import com.example.iticket.model.response.TicketResponse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

import java.util.List;

@Mapper(componentModel = "spring", nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE )
public interface TicketMapper {
    @Mapping(source = "sector.name", target = "sectorName")
    TicketResponse toResponse(TicketEntity entity);

    @Mapping(source = "sector.name", target = "sectorName")
    List<TicketResponse> toResponseList(List<TicketEntity> entity);

}
