package com.startit.itemservice.mapper;

import com.startit.itemservice.entity.FeedbackEntity;
import com.startit.itemservice.transfer.Feedback;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper
public interface FeedbackMapper {
    FeedbackMapper INSTANCE = Mappers.getMapper(FeedbackMapper.class);

    @Mapping(source = "entity.item.id", target = "itemId")
    Feedback toDto(FeedbackEntity entity);
}
