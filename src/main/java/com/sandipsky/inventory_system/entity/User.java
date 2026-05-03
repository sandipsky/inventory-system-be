package com.sandipsky.inventory_system.entity;

import java.util.Collection;
import java.util.Date;
import java.util.List;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
@Table(name = "user")
public class User implements UserDetails {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(nullable = false, unique = true)
    private String username;
    private String fullName;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(nullable = false)
    private String password;
    private String gender;
    private String contact;

    private String imageUrl;

    @Column(name = "is_active")
    private boolean isActive = true;

    @Column(name = "account_non_locked")
    private boolean accountNonLocked = true;
     
    @Column(name = "failed_attempt")
    private int failedAttempt = 0;
     
    @Column(name = "lock_time")
    private Date lockTime;

    @Column(columnDefinition = "TEXT")
    private String remarks;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "role_id")
    private Role role;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of();
    }
}