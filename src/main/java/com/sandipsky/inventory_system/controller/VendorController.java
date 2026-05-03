package com.sandipsky.inventory_system.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.sandipsky.inventory_system.dto.ApiResponse;
import com.sandipsky.inventory_system.dto.VendorDTO;
import com.sandipsky.inventory_system.dto.filter.RequestDTO;
import com.sandipsky.inventory_system.entity.Vendor;
import com.sandipsky.inventory_system.security.RequiresOperation;
import com.sandipsky.inventory_system.service.VendorService;
import com.sandipsky.inventory_system.util.ResponseUtil;

import org.springframework.data.domain.*;
import org.springframework.http.ResponseEntity;

import java.util.List;

@RestController
@RequestMapping("/vendor")
public class VendorController {

    @Autowired
    private VendorService service;

    @GetMapping()
    @RequiresOperation("ViewVendor")
    public List<VendorDTO> getVendors() {
        return service.getVendors();
    }

    @PostMapping("/view")
    @RequiresOperation("ViewVendor")
    public Page<VendorDTO> getPaginatedVendorsList(@RequestBody RequestDTO request) {
        return service.getPaginatedVendorsList(request);
    }

    @GetMapping("/{id}")
    @RequiresOperation("ViewVendor")
    public VendorDTO getVendor(@PathVariable int id) {
        return service.getVendorById(id);
    }

    @PostMapping()
    @RequiresOperation("CreateVendor")
    public ResponseEntity<ApiResponse<Vendor>> createVendor(@RequestBody VendorDTO vendor) {
        Vendor res = service.saveVendor(vendor);
        return ResponseEntity.ok(ResponseUtil.success(res.getId(), "Vendor Added successfully"));
    }

    @PutMapping("/{id}")
    @RequiresOperation("EditVendor")
    public ResponseEntity<ApiResponse<Vendor>> updateVendor(@PathVariable int id, @RequestBody VendorDTO vendor) {
        Vendor res = service.updateVendor(id, vendor);
        return ResponseEntity.ok(ResponseUtil.success(res.getId(), "Vendor Updated successfully"));
    }

    @DeleteMapping("/{id}")
    @RequiresOperation("DeleteVendor")
    public ResponseEntity<ApiResponse<Vendor>> deleteVendor(@PathVariable int id) {
        service.deleteVendor(id);
        return ResponseEntity.ok(ResponseUtil.success(id, "Vendor Deleted successfully"));
    }
}
