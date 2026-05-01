package com.sandipsky.inventory_system.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.sandipsky.inventory_system.entity.AccountType;

public interface AccountTypeRepository extends JpaRepository<AccountType, Integer> {
}
