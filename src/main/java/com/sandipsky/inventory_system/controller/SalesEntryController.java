package com.sandipsky.inventory_system.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.sandipsky.inventory_system.dto.ApiResponse;
import com.sandipsky.inventory_system.dto.sales.MasterSalesEntryDTO;
import com.sandipsky.inventory_system.dto.filter.RequestDTO;
import com.sandipsky.inventory_system.entity.MasterSalesEntry;
import com.sandipsky.inventory_system.service.DocumentNumberingService;
import com.sandipsky.inventory_system.service.SalesEntryService;
import com.sandipsky.inventory_system.util.ResponseUtil;

import org.springframework.data.domain.*;
import org.springframework.http.ResponseEntity;

@RestController
@RequestMapping("/sales")
public class SalesEntryController {

    @Autowired
    private SalesEntryService service;

    @Autowired
    private DocumentNumberingService documentNumberingService;

    @GetMapping("/generateNumber")
    public String generateSalesNumber() {
        return documentNumberingService.generateSalesNumber();
    }

    @PostMapping("/view")
    public Page<MasterSalesEntryDTO> getPaginatedMasterSalesEntrysList(@RequestBody RequestDTO request) {
        return service.getPaginatedMasterSalesEntrysList(request);
    }

    @GetMapping("/{id}")
    public MasterSalesEntryDTO getMasterSalesEntry(@PathVariable int id) {
        return service.getMasterSalesEntryById(id);
    }

    @PostMapping()
    public ResponseEntity<ApiResponse<MasterSalesEntry>> createMasterSalesEntry(@RequestBody MasterSalesEntryDTO masterSalesEntryDTO) {
        MasterSalesEntry res = service.saveMasterSalesEntry(masterSalesEntryDTO);
        return ResponseEntity.ok(ResponseUtil.success(res.getId(), "Sales Added successfully"));
    }

    @PostMapping("/{id}")
    public ResponseEntity<ApiResponse<MasterSalesEntry>> cancelMasterSalesEntry(@PathVariable int id, @RequestBody MasterSalesEntryDTO masterSalesEntryDTO) {
        service.cancelMasterSalesEntry(id, masterSalesEntryDTO);
        return ResponseEntity.ok(ResponseUtil.success(id, "Sales Cancelled successfully"));
    }
}
