package com.sandipsky.inventory_system.features.sales.sales_return.controllers;
import com.sandipsky.inventory_system.features.sales.sales_return.services.SalesReturnService;
import com.sandipsky.inventory_system.features.sales.sales_return.dtos.MasterSalesReturnDTO;
import com.sandipsky.inventory_system.features.sales.sales_return.entities.MasterSalesReturn;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.sandipsky.inventory_system.common.dto.ApiResponse;
import com.sandipsky.inventory_system.common.dto.filter.RequestDTO;
import com.sandipsky.inventory_system.features.settings.document_numbering.services.DocumentNumberingService;
import com.sandipsky.inventory_system.common.util.ResponseUtil;

import org.springframework.data.domain.*;
import org.springframework.http.ResponseEntity;

@RestController
@RequestMapping("/sales-return")
public class SalesReturnController {

    @Autowired
    private SalesReturnService service;

    @Autowired
    private DocumentNumberingService documentNumberingService;

    @GetMapping("/generateNumber")
    public String generateSalesReturnNumber() {
        return documentNumberingService.generateSalesReturnNumber();
    }

    @PostMapping("/view")
    public Page<MasterSalesReturnDTO> getPaginatedMasterSalesReturnsList(@RequestBody RequestDTO request) {
        return service.getPaginatedMasterSalesReturnsList(request);
    }

    @GetMapping("/{id}")
    public MasterSalesReturnDTO getMasterSalesReturn(@PathVariable int id) {
        return service.getMasterSalesReturnById(id);
    }

    @PostMapping()
    public ResponseEntity<ApiResponse<MasterSalesReturn>> createMasterSalesReturn(
            @RequestBody MasterSalesReturnDTO masterSalesReturnDTO) {
        MasterSalesReturn res = service.saveMasterSalesReturn(masterSalesReturnDTO);
        return ResponseEntity.ok(ResponseUtil.success(res.getId(), "Sales Return Added successfully"));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<MasterSalesReturn>> updateMasterSalesReturn(@PathVariable int id,
            @RequestBody MasterSalesReturnDTO masterSalesReturnDTO) {
        MasterSalesReturn res = service.updateMasterSalesReturn(id, masterSalesReturnDTO);
        return ResponseEntity.ok(ResponseUtil.success(res.getId(), "Sales Return Updated successfully"));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<MasterSalesReturn>> deleteMasterSalesReturn(@PathVariable int id) {
        service.deleteMasterSalesReturn(id);
        return ResponseEntity.ok(ResponseUtil.success(id, "Sales Return Deleted successfully"));
    }
}
