package com.sandipsky.inventory_system.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import com.sandipsky.inventory_system.dto.DropdownDTO;
import com.sandipsky.inventory_system.entity.Unit;

public interface UnitRepository extends JpaRepository<Unit, Integer>, JpaSpecificationExecutor<Unit> {
    boolean existsByName(String name);

    boolean existsByNameAndIdNot(String name, int id);

    @Query("""
                SELECT new com.sandipsky.inventory_system.dto.DropdownDTO(u.id, u.name)
                FROM Unit u
                WHERE (:isActive IS NULL OR u.isActive = :isActive)
            """)
    List<DropdownDTO> findFilteredDropdown(
            Boolean isActive);
}
