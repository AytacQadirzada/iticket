package com.example.iticket.mapper;

import com.example.iticket.dao.entity.BasketEntity;
import com.example.iticket.model.response.BasketResponse;
import org.mapstruct.Mapper;
import org.mapstruct.NullValuePropertyMappingStrategy;

@Mapper(componentModel = "spring", nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE )
public interface BasketMapper {
    BasketResponse toResponse(BasketEntity entity);
}
