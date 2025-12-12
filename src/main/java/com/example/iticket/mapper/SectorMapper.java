package com.example.iticket.mapper;

import com.example.iticket.dao.entity.SectorEntity;
import com.example.iticket.model.request.SectorRequest;
import com.example.iticket.model.response.SectorResponse;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

@Mapper(componentModel = "spring", nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE )
public interface SectorMapper {
    SectorEntity toEntity(SectorRequest request);
    SectorResponse toResponse(SectorEntity entity);
    void mapForUpdate(SectorRequest request,@MappingTarget SectorEntity entity);

}
