package com.sandipsky.inventory_system.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import com.sandipsky.inventory_system.entity.Role;

public interface RoleRepository extends JpaRepository<Role, Integer>, JpaSpecificationExecutor<Role> {
    boolean existsByName(String name);

    boolean existsByNameAndIdNot(String name, int id);

    Optional<Role> findByName(String name);
}
