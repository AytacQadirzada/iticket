package com.example.iticket.mapper;

import com.example.iticket.dao.entity.ProductEntity;
import com.example.iticket.model.request.ProductRequest;
import com.example.iticket.model.response.ProductResponse;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

@Mapper(componentModel = "spring", nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE )
public interface ProductMapper {
    ProductEntity toEntity(ProductRequest request);
    ProductResponse toResponse(ProductEntity entity);
    void mapForUpdate(ProductRequest request,@MappingTarget ProductEntity entity);

}
