package com.sandipsky.inventory_system.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import com.sandipsky.inventory_system.entity.Configuration;

public interface ConfigurationRepository
        extends JpaRepository<Configuration, Integer>, JpaSpecificationExecutor<Configuration> {

    Optional<Configuration> findByName(String name);
}
