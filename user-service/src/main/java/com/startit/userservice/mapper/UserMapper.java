package com.startit.userservice.mapper;

import com.startit.userservice.entity.UserEntity;
import com.startit.userservice.transfer.User;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface UserMapper {
    UserMapper INSTANCE = Mappers.getMapper(UserMapper.class);

    User toDto(UserEntity entity);
    UserEntity toEntity(User dto);
}
