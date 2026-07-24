package com.sandipsky.inventory_system.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.sandipsky.inventory_system.dto.ApiResponse;
import com.sandipsky.inventory_system.dto.purchase.MasterPurchaseEntryDTO;
import com.sandipsky.inventory_system.dto.filter.RequestDTO;
import com.sandipsky.inventory_system.entity.MasterPurchaseEntry;
import com.sandipsky.inventory_system.service.DocumentNumberingService;
import com.sandipsky.inventory_system.service.PurchaseEntryService;
import com.sandipsky.inventory_system.util.ResponseUtil;

import org.springframework.data.domain.*;
import org.springframework.http.ResponseEntity;

@RestController
@RequestMapping("/purchase")
public class PurchaseEntryController {

    @Autowired
    private PurchaseEntryService service;

    @Autowired
    private DocumentNumberingService documentNumberingService;

    @GetMapping("/generateNumber")
    public String generatePurchaseNumber() {
        return documentNumberingService.generatePurchaseNumber();
    }

    @PostMapping("/view")
    public Page<MasterPurchaseEntryDTO> getPaginatedMasterPurchaseEntrysList(@RequestBody RequestDTO request) {
        return service.getPaginatedMasterPurchaseEntrysList(request);
    }

    @GetMapping("/{id}")
    public MasterPurchaseEntryDTO getMasterPurchaseEntry(@PathVariable int id) {
        return service.getMasterPurchaseEntryById(id);
    }

    @PostMapping()
    public ResponseEntity<ApiResponse<MasterPurchaseEntry>> createMasterPurchaseEntry(@RequestBody MasterPurchaseEntryDTO masterPurchaseEntryDTO) {
        MasterPurchaseEntry res = service.saveMasterPurchaseEntry(masterPurchaseEntryDTO);
        return ResponseEntity.ok(ResponseUtil.success(res.getId(), "Purchase Added successfully"));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<MasterPurchaseEntry>> updateMasterPurchaseEntry(@PathVariable int id, @RequestBody MasterPurchaseEntryDTO masterPurchaseEntryDTO) {
        MasterPurchaseEntry res = service.updateMasterPurchaseEntry(id, masterPurchaseEntryDTO);
        return ResponseEntity.ok(ResponseUtil.success(res.getId(), "Purchase Updated successfully"));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<MasterPurchaseEntry>> deleteMasterPurchaseEntry(@PathVariable int id) {
        service.deleteMasterPurchaseEntry(id);
        return ResponseEntity.ok(ResponseUtil.success(id, "Purchase Deleted successfully"));
    }
}
