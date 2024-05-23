package com.startit.chatservice.mapper;

import com.startit.chatservice.entity.MessageEntity;
import com.startit.chatservice.transfer.Message;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper(uses = {ChatMapper.class})
public interface MessageMapper {
    MessageMapper INSTANCE = Mappers.getMapper(MessageMapper.class);

    @Mapping(target = "globalSeqNumber", ignore = true)
    Message toDto(MessageEntity entity);
    MessageEntity toEntity(Message entity);
}
