package com.startit.chatservice.entity;

import jakarta.persistence.GeneratedValue;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

@Data
@Table(name = "messages")
public class MessageEntity {
    @Id
    @GeneratedValue
    private Long id;
    private String message;
    private Long chatId;
    private Long senderId;
}
