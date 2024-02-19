package com.startit.authservice.mapper;

import com.startit.authservice.entity.UserEntity;
import com.startit.authservice.transfer.User;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface UserMapper {
    UserMapper INSTANCE = Mappers.getMapper(UserMapper.class);

    User toDto(UserEntity entity);
    UserEntity toEntity(User dto);
}
