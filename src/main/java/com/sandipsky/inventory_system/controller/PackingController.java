package com.sandipsky.inventory_system.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.sandipsky.inventory_system.dto.ApiResponse;
import com.sandipsky.inventory_system.dto.filter.RequestDTO;
import com.sandipsky.inventory_system.entity.Packing;
import com.sandipsky.inventory_system.security.RequiresOperation;
import com.sandipsky.inventory_system.service.PackingService;
import com.sandipsky.inventory_system.util.ResponseUtil;

import java.util.List;

@RestController
@RequestMapping("/master/packings")
public class PackingController {

    @Autowired
    private PackingService service;

    @GetMapping()
    @RequiresOperation("ViewPacking")
    public List<Packing> getPackings() {
        return service.getPackings();
    }

    @PostMapping("/view")
    @RequiresOperation("ViewPacking")
    public Page<Packing> getPaginatedPackingsList(@RequestBody RequestDTO request) {
        return service.getPaginatedPackingsList(request);
    }

    @GetMapping("/{id}")
    @RequiresOperation("ViewPacking")
    public Packing getPacking(@PathVariable int id) {
        return service.getPackingById(id);
    }

    @PostMapping()
    @RequiresOperation("CreatePacking")
    public ResponseEntity<ApiResponse<Packing>> createPacking(@RequestBody Packing packing) {
        Packing res = service.savePacking(packing);
        return ResponseEntity.ok(ResponseUtil.success(res.getId(), "Packing Added successfully"));
    }

    @PutMapping("/{id}")
    @RequiresOperation("EditPacking")
    public ResponseEntity<ApiResponse<Packing>> updatePacking(@PathVariable int id, @RequestBody Packing packing) {
        Packing res = service.updatePacking(id, packing);
        return ResponseEntity.ok(ResponseUtil.success(res.getId(), "Packing Updated successfully"));
    }

    @DeleteMapping("/{id}")
    @RequiresOperation("DeletePacking")
    public ResponseEntity<ApiResponse<Packing>> deletePacking(@PathVariable int id) {
        service.deletePacking(id);
        return ResponseEntity.ok(ResponseUtil.success(id, "Packing Deleted successfully"));
    }
}
