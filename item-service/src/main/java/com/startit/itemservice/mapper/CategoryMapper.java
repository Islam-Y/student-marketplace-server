package com.startit.itemservice.mapper;

import com.startit.itemservice.entity.CategoryEntity;
import com.startit.itemservice.transfer.Category;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface CategoryMapper {
    CategoryMapper INSTANCE = Mappers.getMapper(CategoryMapper.class);

    Category toDto(CategoryEntity entity);
    CategoryEntity toEntity(Category dto);
}
