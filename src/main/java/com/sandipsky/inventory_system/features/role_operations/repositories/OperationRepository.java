package com.sandipsky.inventory_system.role;

import org.springframework.data.jpa.repository.JpaRepository;


public interface OperationRepository extends JpaRepository<Operation, Integer> {
    boolean existsByName(String name);
}
