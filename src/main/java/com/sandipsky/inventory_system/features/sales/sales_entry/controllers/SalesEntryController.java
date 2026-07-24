package com.sandipsky.inventory_system.features.sales.sales_entry.controllers;
import com.sandipsky.inventory_system.features.sales.sales_entry.services.SalesEntryService;
import com.sandipsky.inventory_system.features.sales.sales_entry.dtos.MasterSalesEntryDTO;
import com.sandipsky.inventory_system.features.sales.sales_entry.entities.MasterSalesEntry;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.sandipsky.inventory_system.common.dto.ApiResponse;
import com.sandipsky.inventory_system.common.dto.filter.RequestDTO;
import com.sandipsky.inventory_system.features.settings.document_numbering.services.DocumentNumberingService;
import com.sandipsky.inventory_system.common.util.ResponseUtil;

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
