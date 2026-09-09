package com.countryedu.empapi.dto;

import java.util.List;

public class LoginResponse {
    private String token;
    private String tokenType = "Bearer";
    private String username;
    private List<String> roles;
    private Long expiresIn;

    public LoginResponse() {}

    public LoginResponse(String token, String tokenType, String username, List<String> roles, Long expiresIn) {
        this.token = token;
        this.tokenType = tokenType != null ? tokenType : "Bearer";
        this.username = username;
        this.roles = roles;
        this.expiresIn = expiresIn;
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private String token;
        private String tokenType = "Bearer";
        private String username;
        private List<String> roles;
        private Long expiresIn;

        public Builder token(String token) { this.token = token; return this; }
        public Builder tokenType(String tokenType) { this.tokenType = tokenType; return this; }
        public Builder username(String username) { this.username = username; return this; }
        public Builder roles(List<String> roles) { this.roles = roles; return this; }
        public Builder expiresIn(Long expiresIn) { this.expiresIn = expiresIn; return this; }

        public LoginResponse build() {
            return new LoginResponse(token, tokenType, username, roles, expiresIn);
        }
    }

    public String getToken() { return token; }
    public void setToken(String token) { this.token = token; }
    public String getTokenType() { return tokenType; }
    public void setTokenType(String tokenType) { this.tokenType = tokenType; }
    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }
    public List<String> getRoles() { return roles; }
    public void setRoles(List<String> roles) { this.roles = roles; }
    public Long getExpiresIn() { return expiresIn; }
    public void setExpiresIn(Long expiresIn) { this.expiresIn = expiresIn; }
}
