package com.startit.authservice.transfer;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class User {
    private Long id;
    private String name;
    private String username;
    private String password;
    private String familyName;
    private Role role;

    public User(String username, String password) {
        this.username = username;
        this.password = password;
    }
}
