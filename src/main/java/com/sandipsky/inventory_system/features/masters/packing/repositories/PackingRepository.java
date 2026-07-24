package com.sandipsky.inventory_system.packing;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import com.sandipsky.inventory_system.common.dto.DropdownDTO;

public interface PackingRepository extends JpaRepository<Packing, Integer>, JpaSpecificationExecutor<Packing> {
    boolean existsByName(String name);

    boolean existsByNameAndIdNot(String name, int id);

    @Query("""
                SELECT new com.sandipsky.inventory_system.common.dto.DropdownDTO(p.id, p.name)
                FROM Packing p
                WHERE (:isActive IS NULL OR p.isActive = :isActive)
            """)
    List<DropdownDTO> findFilteredDropdown(
            Boolean isActive);
}
