package com.sandipsky.inventory_system.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import com.sandipsky.inventory_system.entity.Vendor;
import com.sandipsky.inventory_system.dto.DropdownDTO;

public interface VendorRepository extends JpaRepository<Vendor, Integer>, JpaSpecificationExecutor<Vendor> {
    boolean existsByName(String name);

    boolean existsByNameAndIdNot(String name, int id);

    @Query("""
            SELECT new com.sandipsky.inventory_system.dto.DropdownDTO(v.id, v.name)
            FROM Vendor v
            WHERE (:isActive IS NULL OR v.isActive = :isActive)
        """)
    List<DropdownDTO> findFilteredDropdown(Boolean isActive);
}
