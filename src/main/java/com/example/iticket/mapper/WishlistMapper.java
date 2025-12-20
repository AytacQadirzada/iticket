package com.example.iticket.mapper;

import com.example.iticket.dao.entity.WishlistEntity;
import com.example.iticket.model.response.WishlistResponse;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

@Mapper(componentModel = "spring", nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE )
public interface WishlistMapper {
//    WishlistEntity toEntity(WishlistRequest request);
    WishlistResponse toResponse(WishlistEntity entity);
//    void mapForUpdate(WishlistRequest response,@MappingTarget WishlistEntity entity);

}
