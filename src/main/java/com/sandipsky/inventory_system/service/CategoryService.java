package com.sandipsky.inventory_system.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import com.sandipsky.inventory_system.dto.filter.RequestDTO;
import com.sandipsky.inventory_system.entity.Category;
import com.sandipsky.inventory_system.exception.DuplicateResourceException;
import com.sandipsky.inventory_system.exception.ResourceNotFoundException;
import com.sandipsky.inventory_system.repository.CategoryRepository;
import com.sandipsky.inventory_system.util.SpecificationBuilder;

import java.util.List;

@Service
public class CategoryService {

    @Autowired
    private CategoryRepository repository;

    private final SpecificationBuilder<Category> specBuilder = new SpecificationBuilder<>();

    public Category saveCategory(Category category) {
        if (category.getName() == null || category.getName().trim().isEmpty()) {
            throw new RuntimeException("Category name cannot be null or blank");
        }
        if (repository.existsByName(category.getName().trim())) {
            throw new DuplicateResourceException("Category with the same name already exists");
        }
        category.setName(category.getName().trim());
        return repository.save(category);
    }

    public Page<Category> getPaginatedCategorysList(RequestDTO request) {
        Pageable pageable = PageRequest.of(
                request.getPagination() != null ? request.getPagination().getPageIndex() : 0,
                request.getPagination() != null ? request.getPagination().getPageSize() : 25,
                specBuilder.buildSort(request.getSortDTO()));

        Specification<Category> spec = specBuilder.buildSpecification(request.getFilter());
        Page<Category> categoryPage = repository.findAll(spec, pageable);
        return categoryPage;
    }

    public List<Category> getCategorys() {
        return repository.findAll();
    }

    public Category getCategoryById(int id) {
        Category existing = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Category not found"));
        return existing;
    }

    public Category updateCategory(int id, Category category) {
        if (category.getName() == null || category.getName().trim().isEmpty()) {
            throw new RuntimeException("Category name cannot be null or blank");
        }
        Category existing = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Category not found"));

        if (repository.existsByNameAndIdNot(category.getName().trim(), id)) {
            throw new DuplicateResourceException("Category with the same name already exists");
        }
        existing.setName(category.getName().trim());
        existing.setActive(category.isActive());
        return repository.save(existing);
    }

    public void deleteCategory(int id) {
        repository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Category not found"));
        repository.deleteById(id);
    }
}
