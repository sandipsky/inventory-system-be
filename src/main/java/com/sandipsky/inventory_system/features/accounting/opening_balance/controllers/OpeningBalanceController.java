package com.sandipsky.inventory_system.features.accounting.opening_balance.controllers;
import com.sandipsky.inventory_system.features.accounting.opening_balance.services.OpeningBalanceService;
import com.sandipsky.inventory_system.features.accounting.journal.dtos.MasterJournalEntryDTO;
import com.sandipsky.inventory_system.features.accounting.journal.entities.MasterJournalEntry;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.sandipsky.inventory_system.common.dto.ApiResponse;
import com.sandipsky.inventory_system.common.util.ResponseUtil;

import org.springframework.http.ResponseEntity;

@RestController
@RequestMapping("/opening-balance")
public class OpeningBalanceController {

    @Autowired
    private OpeningBalanceService service;

    @GetMapping()
    public MasterJournalEntryDTO getOpeningBalance() {
        return service.getOpeningBalance();
    }

    @PostMapping()
    public ResponseEntity<ApiResponse<MasterJournalEntry>> saveOpeningBalance(
            @RequestBody MasterJournalEntryDTO masterJournalEntryDTO) {
        MasterJournalEntry res = service.saveOpeningBalance(masterJournalEntryDTO);
        return ResponseEntity.ok(ResponseUtil.success(res.getId(), "Opening Balance Saved successfully"));
    }
}
