package com.startit.chatservice.entity;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.domain.Persistable;
import org.springframework.data.relational.core.mapping.Table;

@Data
@Table(name = "users_chat_ms")
public class UserEntity implements Persistable<Long> {

    @Id
    private Long id;
    private String name;
    private String username;
    private String password;
    private String familyName;
    private String role;

    @Override
    public boolean isNew() {
        return true;
    }
}
