package com.example.iticket.mapper;

import com.example.iticket.dao.entity.CategoryEntity;
import com.example.iticket.model.request.CategoryRequest;
import com.example.iticket.model.response.CategoryResponse;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

@Mapper(componentModel = "spring", nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE )
public interface CategoryMapper {
    CategoryEntity toEntity(CategoryRequest request);
    CategoryResponse toResponse(CategoryEntity entity);
    void mapForUpdate(CategoryRequest response,@MappingTarget CategoryEntity entity);

}
