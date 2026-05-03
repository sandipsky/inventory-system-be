package com.sandipsky.inventory_system.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.sandipsky.inventory_system.dto.ApiResponse;
import com.sandipsky.inventory_system.dto.filter.RequestDTO;
import com.sandipsky.inventory_system.entity.TaxType;
import com.sandipsky.inventory_system.security.RequiresOperation;
import com.sandipsky.inventory_system.service.TaxTypeService;
import com.sandipsky.inventory_system.util.ResponseUtil;

import java.util.List;

@RestController
@RequestMapping("/master/taxtypes")
public class TaxTypeController {

    @Autowired
    private TaxTypeService service;

    @GetMapping()
    @RequiresOperation("ViewTaxType")
    public List<TaxType> getTaxTypes() {
        return service.getTaxTypes();
    }

    @PostMapping("/view")
    @RequiresOperation("ViewTaxType")
    public Page<TaxType> getPaginatedTaxTypesList(@RequestBody RequestDTO request) {
        return service.getPaginatedTaxTypesList(request);
    }

    @GetMapping("/{id}")
    @RequiresOperation("ViewTaxType")
    public TaxType getTaxType(@PathVariable int id) {
        return service.getTaxTypeById(id);
    }

    @PostMapping()
    @RequiresOperation("CreateTaxType")
    public ResponseEntity<ApiResponse<TaxType>> createTaxType(@RequestBody TaxType taxType) {
        TaxType res = service.saveTaxType(taxType);
        return ResponseEntity.ok(ResponseUtil.success(res.getId(), "Tax Type Added successfully"));
    }

    @PutMapping("/{id}")
    @RequiresOperation("EditTaxType")
    public ResponseEntity<ApiResponse<TaxType>> updateTaxType(@PathVariable int id, @RequestBody TaxType taxType) {
        TaxType res = service.updateTaxType(id, taxType);
        return ResponseEntity.ok(ResponseUtil.success(res.getId(), "Tax Type Updated successfully"));
    }

    @DeleteMapping("/{id}")
    @RequiresOperation("DeleteTaxType")
    public ResponseEntity<ApiResponse<TaxType>> deleteTaxType(@PathVariable int id) {
        service.deleteTaxType(id);
        return ResponseEntity.ok(ResponseUtil.success(id, "Tax Type Deleted successfully"));
    }
}
