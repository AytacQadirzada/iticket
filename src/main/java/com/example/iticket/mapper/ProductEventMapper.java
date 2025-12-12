package com.example.iticket.mapper;

import com.example.iticket.dao.entity.HallEntity;
import com.example.iticket.dao.entity.ProductEventEntity;
import com.example.iticket.model.request.HallRequest;
import com.example.iticket.model.request.ProductEventRequest;
import com.example.iticket.model.response.HallResponse;
import com.example.iticket.model.response.ProductEventResponse;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

@Mapper(componentModel = "spring", nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE )
public interface ProductEventMapper {
    ProductEventEntity toEntity(ProductEventRequest request);
    ProductEventResponse toResponse(ProductEventEntity entity);
    void mapForUpdate(ProductEventRequest request,@MappingTarget ProductEventEntity entity);

}
