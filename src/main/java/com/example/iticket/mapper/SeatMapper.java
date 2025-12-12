package com.example.iticket.mapper;

import com.example.iticket.dao.entity.SeatEntity;
import com.example.iticket.model.request.SeatRequest;
import com.example.iticket.model.response.SeatResponse;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

@Mapper(componentModel = "spring", nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE )
public interface SeatMapper {
    SeatEntity toEntity(SeatRequest request);
    SeatResponse toResponse(SeatEntity entity);
    void mapForUpdate(SeatRequest request,@MappingTarget SeatEntity entity);

}
