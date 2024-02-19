package com.startit.authservice.transfer;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class AuthResponse {
    private boolean isValid;
    private String errorMsg;
    private String accessToken;
    private String refreshToken;

    public static AuthResponse err(String errorMsg) {
        return AuthResponse.builder()
                .isValid(false)
                .errorMsg(errorMsg)
                .build();
    }
}
