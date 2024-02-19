package com.startit.chatservice.entity;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

@Data
@Table(name = "messages")
public class MessageEntity {
    @Id
    private Long id;
    private String message;
    private Long seqNumber;
    private Long chatId;
    private Long senderId;
}
