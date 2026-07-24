package com.sandipsky.inventory_system.auth;

import lombok.Data;

@Data
public class LoginResponse {
    String token;

    public LoginResponse(String token) {
        this.token = token;
    }
}
