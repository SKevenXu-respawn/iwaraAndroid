package com.sk.iwara.payload;

public class TokenPayload {
    String token;
    String accessToken;

    public String getAccess_token() {
        return accessToken;
    }

    public String getToken() {
        return token;
    }

    public void setAccess_token(String accessToken) {
        this.accessToken = accessToken;
    }

    public void setToken(String token) {
        this.token = token;
    }
}
