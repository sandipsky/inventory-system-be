package com.sandipsky.inventory_system.service;

import com.sandipsky.inventory_system.repository.*;
import com.sandipsky.inventory_system.entity.*;
import com.sandipsky.inventory_system.exception.AccountLockException;
import com.sandipsky.inventory_system.exception.ResourceNotFoundException;
import com.sandipsky.inventory_system.dto.UserRoleOperationsDTO;
import com.sandipsky.inventory_system.dto.login.*;

import java.util.LinkedHashSet;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Service;

@Service
public class AuthService {
        @Autowired
        private UserRepository userRepository;

        @Autowired
        private AuthenticationManager authenticationManager;

        @Value("${auth.maxFailedAttempts}")
        private int maxFailedAttempts;

        @Value("${auth.lockTimeDurationMs}")
        private long lockTimeDurationMs;

        public User authenticate(LoginRequest request) {
                User user = userRepository.findByUsername(request.getUsername())
                                .orElseThrow(() -> new RuntimeException("User not found"));

                if (!user.isActive()) {
                        throw new RuntimeException("User account is inactive.");
                }

                if (!user.isAccountNonLocked()) {
                        if (user.getLockTime() != null) {
                                long lockTimeInMillis = user.getLockTime().getTime();
                                long currentTimeInMillis = System.currentTimeMillis();

                                if (currentTimeInMillis - lockTimeInMillis >= lockTimeDurationMs) {
                                        // Unlock the account
                                        user.setAccountNonLocked(true);
                                        user.setFailedAttempt(0);
                                        user.setLockTime(null);
                                        userRepository.save(user);
                                } else {
                                        throw new AccountLockException("Account is locked. Try again later.");
                                }
                        }
                        else {
                                throw new AccountLockException("Account is locked. Try again later.");
                        }
                }

                try {
                        authenticationManager.authenticate(
                                        new UsernamePasswordAuthenticationToken(
                                                        request.getUsername(),
                                                        request.getPassword()));
                } catch (Exception ex) {
                        int attempts = user.getFailedAttempt() + 1;
                        user.setFailedAttempt(attempts);

                        if (attempts >= maxFailedAttempts) {
                                user.setAccountNonLocked(false);
                                user.setLockTime(new java.util.Date());
                        }

                        userRepository.save(user);
                        throw new BadCredentialsException("Username or Password is Incorrect.");
                }

                user.setFailedAttempt(0);
                user.setAccountNonLocked(true);
                user.setLockTime(null);
                userRepository.save(user);
                return user;
        }

        public UserRoleOperationsDTO getUserRoleOperations(String username) {
                User user = userRepository.findByUsername(username)
                                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

                UserRoleOperationsDTO dto = new UserRoleOperationsDTO();
                dto.setId(user.getId());
                dto.setUsername(user.getUsername());
                dto.setFullName(user.getFullName());
                dto.setImageUrl(user.getImageUrl());

                Role role = user.getRole();
                if (role == null) {
                        return dto;
                }

                dto.setRoleId(role.getId());
                dto.setRoleName(role.getName());

                Set<String> masterModules = new LinkedHashSet<>();
                Set<String> modules = new LinkedHashSet<>();
                Set<String> operations = new LinkedHashSet<>();

                if (role.getOperations() != null) {
                        for (Operation op : role.getOperations()) {
                                masterModules.add(op.getMasterModule());
                                modules.add(op.getModule());
                                operations.add(op.getName());
                        }
                }

                dto.getMasterModules().addAll(masterModules);
                dto.getModules().addAll(modules);
                dto.getOperations().addAll(operations);
                return dto;
        }
}
