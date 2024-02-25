package com.startit.chatservice.mapper;

import com.startit.chatservice.entity.UserEntity;
import com.startit.shared.transfer.Role;
import com.startit.shared.transfer.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.mapstruct.factory.Mappers;

@Mapper
public interface UserMapper {

    UserMapper INSTANCE = Mappers.getMapper(UserMapper.class);

    @Mapping(target = "role", source = "role", qualifiedByName = "stringToRole")
    User toDto(UserEntity entity);

    @Mapping(target = "role", source = "role", qualifiedByName = "roleToString")
    UserEntity toEntity(User dto);

    @Named(value = "roleToString") // Ensure the name matches the annotation
    default String roleToString(Role value) {
        return value == null ? null : value.toString();
    }

    @Named(value = "stringToRole") // Ensure the name matches the annotation
    default Role stringToRole(String value) {
        return value == null ? null : Role.valueOf(value.toUpperCase());
    }
}
