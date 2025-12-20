package com.example.iticket.mapper;

import com.example.iticket.dao.entity.BasketItemEntity;
import com.example.iticket.model.request.BasketItemRequest;
import com.example.iticket.model.response.BasketItemResponse;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

@Mapper(componentModel = "spring", nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE )
public interface BasketItemMapper {
    BasketItemEntity toEntity(BasketItemRequest request);
    BasketItemResponse toResponse(BasketItemEntity entity);
    void mapForUpdate(BasketItemRequest response,@MappingTarget BasketItemEntity entity);

}
