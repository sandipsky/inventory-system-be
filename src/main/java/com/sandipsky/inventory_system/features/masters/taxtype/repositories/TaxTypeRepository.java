package com.sandipsky.inventory_system.taxtype;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import com.sandipsky.inventory_system.common.dto.DropdownDTO;

public interface TaxTypeRepository extends JpaRepository<TaxType, Integer>, JpaSpecificationExecutor<TaxType> {
    boolean existsByName(String name);

    boolean existsByNameAndIdNot(String name, int id);

    @Query("""
                SELECT new com.sandipsky.inventory_system.common.dto.DropdownDTO(t.id, t.name)
                FROM TaxType t
                WHERE (:isActive IS NULL OR t.isActive = :isActive)
            """)
    List<DropdownDTO> findFilteredDropdown(
            Boolean isActive);
}
