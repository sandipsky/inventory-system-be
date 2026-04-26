package com.sandipsky.inventory_system.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.sandipsky.inventory_system.entity.Packing;
import com.sandipsky.inventory_system.exception.DuplicateResourceException;
import com.sandipsky.inventory_system.exception.ResourceNotFoundException;
import com.sandipsky.inventory_system.repository.PackingRepository;

import java.util.List;

@Service
public class PackingService {

    @Autowired
    private PackingRepository repository;

    public Packing savePacking(Packing packing) {
        if (packing.getName() == null || packing.getName().trim().isEmpty()) {
            throw new RuntimeException("Packing name cannot be null or blank");
        }
        if (repository.existsByName(packing.getName().trim())) {
            throw new DuplicateResourceException("Packing with the same name already exists");
        }
        packing.setName(packing.getName().trim());
        return repository.save(packing);
    }

    public List<Packing> getPackings() {
        return repository.findAll();
    }

    public Packing getPackingById(int id) {
        Packing existing = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Packing not found"));
        return existing;
    }

    public Packing updatePacking(int id, Packing packing) {
        if (packing.getName() == null || packing.getName().trim().isEmpty()) {
            throw new RuntimeException("Packing name cannot be null or blank");
        }
        Packing existing = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Packing not found"));

        if (repository.existsByNameAndIdNot(packing.getName().trim(), id)) {
            throw new DuplicateResourceException("Packing with the same name already exists");
        }
        existing.setName(packing.getName().trim());
        existing.setActive(packing.isActive());
        return repository.save(existing);
    }

    public void deletePacking(int id) {
        repository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Packing not found"));
        repository.deleteById(id);
    }
}
