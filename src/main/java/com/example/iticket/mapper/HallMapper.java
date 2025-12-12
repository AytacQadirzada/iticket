package com.example.iticket.mapper;

import com.example.iticket.dao.entity.CategoryEntity;
import com.example.iticket.dao.entity.HallEntity;
import com.example.iticket.model.request.CategoryRequest;
import com.example.iticket.model.request.HallRequest;
import com.example.iticket.model.response.CategoryResponse;
import com.example.iticket.model.response.HallResponse;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

@Mapper(componentModel = "spring", nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE )
public interface HallMapper {
    HallEntity toEntity(HallRequest request);
    HallResponse toResponse(HallEntity entity);
    void mapForUpdate(HallRequest request,@MappingTarget HallEntity entity);
}