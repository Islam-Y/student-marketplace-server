package com.startit.itemservice.mapper;

import com.startit.shared.transfer.User;
import com.startit.itemservice.entity.UserEntity;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface UserMapper {
    UserMapper INSTANCE = Mappers.getMapper(UserMapper.class);

    User toDto(UserEntity entity);
    UserEntity toEntity(User dto);
}
