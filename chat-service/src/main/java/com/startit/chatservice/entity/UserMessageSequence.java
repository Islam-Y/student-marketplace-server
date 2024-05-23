package com.startit.chatservice.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

@Data
@Entity
@Table(name = "message_sequence")
public class UserMessageSequence {

    @Id
    @GeneratedValue
    private Long id;

    private Long messageId;

    private Long userId;

    private Long seqNumber;
}
