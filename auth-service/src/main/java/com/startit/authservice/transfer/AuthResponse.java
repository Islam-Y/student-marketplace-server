package com.startit.authservice.transfer;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AuthResponse {
    private boolean isValid;
    private String errorMsg;
    private String accessToken;
    private String refreshToken;

    public AuthResponse(boolean isValid, String accessToken, String refreshToken) {
        this.isValid = isValid;
        this.accessToken = accessToken;
        this.refreshToken = refreshToken;
    }

    public AuthResponse(String accessToken, String refreshToken) {
        this.accessToken = accessToken;
        this.refreshToken = refreshToken;
    }
}
