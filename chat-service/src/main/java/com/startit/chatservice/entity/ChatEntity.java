package com.startit.chatservice.entity;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

@Data
@Table(name = "chats")
public class ChatEntity {
    @Id
    private Long id;
    private Long itemId;
    private Long customerId;
}
