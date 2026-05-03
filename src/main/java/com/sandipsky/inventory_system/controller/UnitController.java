package com.sandipsky.inventory_system.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.sandipsky.inventory_system.dto.ApiResponse;
import com.sandipsky.inventory_system.dto.filter.RequestDTO;
import com.sandipsky.inventory_system.entity.Unit;
import com.sandipsky.inventory_system.security.RequiresOperation;
import com.sandipsky.inventory_system.service.UnitService;
import com.sandipsky.inventory_system.util.ResponseUtil;

import java.util.List;

@RestController
@RequestMapping("/master/units")
public class UnitController {

    @Autowired
    private UnitService service;

    @GetMapping()
    @RequiresOperation("ViewUnit")
    public List<Unit> getUnits() {
        return service.getUnits();
    }

    @PostMapping("/view")
    @RequiresOperation("ViewUnit")
    public Page<Unit> getPaginatedUnitsList(@RequestBody RequestDTO request) {
        return service.getPaginatedUnitsList(request);
    }

    @GetMapping("/{id}")
    @RequiresOperation("ViewUnit")
    public Unit getUnit(@PathVariable int id) {
        return service.getUnitById(id);
    }

    @PostMapping()
    @RequiresOperation("CreateUnit")
    public ResponseEntity<ApiResponse<Unit>> createUnit(@RequestBody Unit unit) {
        Unit res = service.saveUnit(unit);
        return ResponseEntity.ok(ResponseUtil.success(res.getId(), "Unit Added successfully"));
    }

    @PutMapping("/{id}")
    @RequiresOperation("EditUnit")
    public ResponseEntity<ApiResponse<Unit>> updateUnit(@PathVariable int id, @RequestBody Unit unit) {
        Unit res = service.updateUnit(id, unit);
        return ResponseEntity.ok(ResponseUtil.success(res.getId(), "Unit Updated successfully"));
    }

    @DeleteMapping("/{id}")
    @RequiresOperation("DeleteUnit")
    public ResponseEntity<ApiResponse<Unit>> deleteUnit(@PathVariable int id) {
        service.deleteUnit(id);
        return ResponseEntity.ok(ResponseUtil.success(id, "Unit Deleted successfully"));
    }
}
