package com.startit.itemservice.mapper;

import com.startit.itemservice.entity.LocationEntity;
import com.startit.itemservice.transfer.Location;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface LocationMapper {
    LocationMapper INSTANCE = Mappers.getMapper(LocationMapper.class);

    Location toDto(LocationEntity entity);
    LocationEntity toEntity(Location dto);
}
