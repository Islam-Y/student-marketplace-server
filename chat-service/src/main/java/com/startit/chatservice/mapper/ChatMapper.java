package com.startit.chatservice.mapper;

import com.startit.chatservice.entity.ChatEntity;
import com.startit.chatservice.transfer.Chat;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface ChatMapper {
    ChatMapper INSTANCE = Mappers.getMapper(ChatMapper.class);

    Chat toDto(ChatEntity entity);
    ChatEntity toEntity(Chat dto);
}
