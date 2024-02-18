package com.startit.itemservice.mapper;

import com.startit.itemservice.entity.CategoryEntity;
import com.startit.itemservice.entity.ItemEntity;
import com.startit.itemservice.transfer.Item;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

import java.util.Collections;
import java.util.List;

@Mapper
public interface ItemMapper {
    ItemMapper INSTANCE = Mappers.getMapper(ItemMapper.class);

    @Mapping(source = "status.id", target = "statusId")
    @Mapping(source = "location.id", target = "locationId")
    @Mapping(source = "categories", target = "categoriesIds")
    Item toDto(ItemEntity itemEntity);

    @Mapping(source = "statusId", target = "status.id")
    @Mapping(source = "locationId", target = "location.id")
    @Mapping(source = "categoriesIds", target = "categories")
    ItemEntity toEntity(Item item);

    default List<Long> mapCategories(List<CategoryEntity> categories) {
        if (categories == null)
            return Collections.emptyList();

        return categories.stream()
                .map(CategoryEntity::getId)
                .toList();
    }

    default List<CategoryEntity> mapCategoryIds(List<Long> categoryIds) {
        if (categoryIds == null)
            return Collections.emptyList();

        return categoryIds.stream()
                .map(id -> {
                    CategoryEntity category = new CategoryEntity();
                    category.setId(id);
                    return category;
                })
                .toList();
    }
}

