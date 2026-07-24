package com.sandipsky.inventory_system.features.purchase.purchase_entry.controllers;
import com.sandipsky.inventory_system.features.purchase.purchase_entry.services.PurchaseEntryService;
import com.sandipsky.inventory_system.features.purchase.purchase_entry.dtos.MasterPurchaseEntryDTO;
import com.sandipsky.inventory_system.features.purchase.purchase_entry.entities.MasterPurchaseEntry;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.sandipsky.inventory_system.common.dto.ApiResponse;
import com.sandipsky.inventory_system.common.dto.filter.RequestDTO;
import com.sandipsky.inventory_system.features.settings.document_numbering.services.DocumentNumberingService;
import com.sandipsky.inventory_system.common.util.ResponseUtil;

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
