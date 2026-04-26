package com.sandipsky.inventory_system.dto.login;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data
public class LoginResponse {
    int id;

    String username;

    @JsonProperty("name")
    String fullName;

    String token;

    public LoginResponse(int id, String username, String fullName, String token) {
        this.username = username;
        this.fullName = fullName;
        this.token = token;
        this.id = id;
    }
}
