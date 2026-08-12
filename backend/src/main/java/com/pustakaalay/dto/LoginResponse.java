package com.pustakaalay.dto;

public class LoginResponse {

    private final String token;
    private final String tokenType;
    private final Long userId;
    private final String email;
    private final String role;

    public LoginResponse(
            String token,
            Long userId,
            String email,
            String role
    ) {
        this.token = token;
        this.tokenType = "Bearer";
        this.userId = userId;
        this.email = email;
        this.role = role;
    }

    public String getToken() {
        return token;
    }

    public String getTokenType() {
        return tokenType;
    }

    public Long getUserId() {
        return userId;
    }

    public String getEmail() {
        return email;
    }

    public String getRole() {
        return role;
    }
}
