package com.example.iticket.mapper;

import com.example.iticket.dao.entity.UserEntity;
import com.example.iticket.model.request.RegisterUserRequest;
import com.example.iticket.model.request.UserRequest;
import com.example.iticket.model.response.AuthResponse;
import com.example.iticket.model.response.UserResponse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

@Mapper(componentModel = "spring", nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE )
public interface UserMapper {

    UserEntity toEntity(RegisterUserRequest request);

    UserResponse toUserResponse(UserEntity entity);

    AuthResponse toResponse(UserEntity entity);
    void mapForUpdate(UserRequest response, @MappingTarget UserEntity entity);

}
