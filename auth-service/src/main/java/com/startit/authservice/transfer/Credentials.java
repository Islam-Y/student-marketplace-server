package com.startit.authservice.transfer;

import lombok.Data;

@Data
public class Credentials {
    private User user;
    private String password;
}
