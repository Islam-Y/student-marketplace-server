package com.startit.chatservice.entity;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

@Data
@Table(name = "users_chat_ms")
public class UserEntity {

    @Id
    private Long id;
    private String name;
    private String username;
    private String password;
    private String familyName;
    private Integer isuNumber;
    private String role;
}
