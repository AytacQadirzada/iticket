package com.example.iticket.mapper;

import com.example.iticket.dao.entity.CategoryEntity;
import com.example.iticket.dao.entity.VenuesEntity;
import com.example.iticket.model.request.CategoryRequest;
import com.example.iticket.model.request.VenuesRequest;
import com.example.iticket.model.response.CategoryResponse;
import com.example.iticket.model.response.VenuesResponse;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

@Mapper(componentModel = "spring", nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE )
public interface VenuesMapper {
    VenuesEntity toEntity(VenuesRequest request);
    VenuesResponse toResponse(VenuesEntity entity);
    void mapForUpdate(VenuesRequest response,@MappingTarget VenuesEntity entity);

}
