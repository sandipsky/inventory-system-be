package com.sandipsky.inventory_system.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.sandipsky.inventory_system.entity.TaxType;
import com.sandipsky.inventory_system.exception.DuplicateResourceException;
import com.sandipsky.inventory_system.exception.ResourceNotFoundException;
import com.sandipsky.inventory_system.repository.TaxTypeRepository;

import java.util.List;

@Service
public class TaxTypeService {

    @Autowired
    private TaxTypeRepository repository;

    public TaxType saveTaxType(TaxType taxType) {
        if (taxType.getName() == null || taxType.getName().trim().isEmpty()) {
            throw new RuntimeException("Tax Type name cannot be null or blank");
        }
        if (repository.existsByName(taxType.getName().trim())) {
            throw new DuplicateResourceException("Tax Type with the same name already exists");
        }
        taxType.setName(taxType.getName().trim());
        return repository.save(taxType);
    }

    public List<TaxType> getTaxTypes() {
        return repository.findAll();
    }

    public TaxType getTaxTypeById(int id) {
        TaxType existing = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Tax Type not found"));
        return existing;
    }

    public TaxType updateTaxType(int id, TaxType taxType) {
        if (taxType.getName() == null || taxType.getName().trim().isEmpty()) {
            throw new RuntimeException("Tax Type name cannot be null or blank");
        }
        TaxType existing = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Tax Type not found"));

        if (repository.existsByNameAndIdNot(taxType.getName().trim(), id)) {
            throw new DuplicateResourceException("Tax Type with the same name already exists");
        }
        existing.setName(taxType.getName().trim());
        existing.setTaxRate(taxType.getTaxRate());
        existing.setActive(taxType.isActive());
        return repository.save(existing);
    }

    public void deleteTaxType(int id) {
        repository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Tax Type not found"));
        repository.deleteById(id);
    }
}
