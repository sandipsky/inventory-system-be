package com.sandipsky.inventory_system.features.documentnumbering.repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;


public interface DocumentNumberingRepository
        extends JpaRepository<DocumentNumbering, Integer>, JpaSpecificationExecutor<DocumentNumbering> {

    Optional<DocumentNumbering> findByName(String name);
}
