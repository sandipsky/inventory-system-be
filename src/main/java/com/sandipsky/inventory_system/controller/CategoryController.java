package com.sandipsky.inventory_system.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.sandipsky.inventory_system.dto.ApiResponse;
import com.sandipsky.inventory_system.dto.filter.RequestDTO;
import com.sandipsky.inventory_system.entity.Category;
import com.sandipsky.inventory_system.service.CategoryService;
import com.sandipsky.inventory_system.util.ResponseUtil;

import java.util.List;

@RestController
@RequestMapping("/master/categorys")
public class CategoryController {

    @Autowired
    private CategoryService service;

    @GetMapping()
    public List<Category> getCategorys() {
        return service.getCategorys();
    }

    @PostMapping("/view")
    public Page<Category> getPaginatedCategorysList(@RequestBody RequestDTO request) {
        return service.getPaginatedCategorysList(request);
    }

    @GetMapping("/{id}")
    public Category getCategory(@PathVariable int id) {
        return service.getCategoryById(id);
    }

    @PostMapping()
    public ResponseEntity<ApiResponse<Category>> createCategory(@RequestBody Category category) {
        Category res = service.saveCategory(category);
        return ResponseEntity.ok(ResponseUtil.success(res.getId(), "Category Added successfully"));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<Category>> updateCategory(@PathVariable int id, @RequestBody Category category) {
        Category res = service.updateCategory(id, category);
        return ResponseEntity.ok(ResponseUtil.success(res.getId(), "Category Updated successfully"));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Category>> deleteCategory(@PathVariable int id) {
        service.deleteCategory(id);
        return ResponseEntity.ok(ResponseUtil.success(id, "Category Deleted successfully"));
    }
}
