package com.sandipsky.inventory_system.controller;

import com.sandipsky.inventory_system.entity.User;
import com.sandipsky.inventory_system.service.AuthService;
import com.sandipsky.inventory_system.dto.login.LoginRequest;
import com.sandipsky.inventory_system.dto.login.LoginResponse;
import com.sandipsky.inventory_system.util.JwtUtil;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("")
public class AuthController {

    @Autowired
    AuthService authService;

    @Autowired
    JwtUtil jwtUtils;

    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(@RequestBody LoginRequest user) {
        User authenticatedUser = authService.authenticate(user);

        String jwtToken = jwtUtils.generateToken(authenticatedUser);

        LoginResponse loginResponse = new LoginResponse(authenticatedUser.getId(), authenticatedUser.getUsername(), authenticatedUser.getFullName(), jwtToken);
        return ResponseEntity.ok(loginResponse);
    }
}
