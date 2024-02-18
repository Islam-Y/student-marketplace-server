package com.startit.itemservice.mapper;

import com.startit.itemservice.entity.StatusEntity;
import com.startit.itemservice.transfer.Status;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface StatusMapper {
    StatusMapper INSTANCE = Mappers.getMapper(StatusMapper.class);

    Status toDto(StatusEntity entity);
    StatusEntity toEntity(Status dto);
}
