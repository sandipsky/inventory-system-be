package com.sandipsky.inventory_system.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.sandipsky.inventory_system.entity.Operation;

public interface OperationRepository extends JpaRepository<Operation, Integer> {
    boolean existsByName(String name);
}
