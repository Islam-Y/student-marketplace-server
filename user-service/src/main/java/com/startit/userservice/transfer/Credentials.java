package com.startit.userservice.transfer;

import com.startit.shared.transfer.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class Credentials {
    private User user;
    private String password;

    public Credentials(String username, String password) {
        this.user = User.builder().username(username).build();
        this.password = password;
    }
}
