package com.sandipsky.inventory_system.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.sandipsky.inventory_system.dto.ApiResponse;
import com.sandipsky.inventory_system.entity.TaxType;
import com.sandipsky.inventory_system.service.TaxTypeService;
import com.sandipsky.inventory_system.util.ResponseUtil;

import java.util.List;

@RestController
@RequestMapping("/tax-types")
public class TaxTypeController {

    @Autowired
    private TaxTypeService service;

    @GetMapping()
    public List<TaxType> getTaxTypes() {
        return service.getTaxTypes();
    }

    @GetMapping("/{id}")
    public TaxType getTaxType(@PathVariable int id) {
        return service.getTaxTypeById(id);
    }

    @PostMapping()
    public ResponseEntity<ApiResponse<TaxType>> createTaxType(@RequestBody TaxType taxType) {
        TaxType res = service.saveTaxType(taxType);
        return ResponseEntity.ok(ResponseUtil.success(res.getId(), "Tax Type Added successfully"));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<TaxType>> updateTaxType(@PathVariable int id, @RequestBody TaxType taxType) {
        TaxType res = service.updateTaxType(id, taxType);
        return ResponseEntity.ok(ResponseUtil.success(res.getId(), "Tax Type Updated successfully"));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<TaxType>> deleteTaxType(@PathVariable int id) {
        service.deleteTaxType(id);
        return ResponseEntity.ok(ResponseUtil.success(id, "Tax Type Deleted successfully"));
    }
}
