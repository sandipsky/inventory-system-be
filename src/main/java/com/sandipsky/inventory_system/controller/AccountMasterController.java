package com.sandipsky.inventory_system.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.sandipsky.inventory_system.dto.ApiResponse;
import com.sandipsky.inventory_system.dto.AccountMasterDTO;
import com.sandipsky.inventory_system.dto.AccountTypeGroupDTO;
import com.sandipsky.inventory_system.dto.DropdownDTO;
import com.sandipsky.inventory_system.dto.filter.RequestDTO;
import com.sandipsky.inventory_system.entity.AccountMaster;
import com.sandipsky.inventory_system.service.AccountMasterService;
import com.sandipsky.inventory_system.util.ResponseUtil;

import org.springframework.data.domain.*;
import org.springframework.http.ResponseEntity;

import java.util.List;

@RestController
@RequestMapping("/accountMaster")
public class AccountMasterController {

    @Autowired
    private AccountMasterService service;

    @GetMapping()
    public List<AccountMasterDTO> getAccountMasters() {
        return service.getAccountMasters();
    }

    @PostMapping("/view")
    public Page<AccountMasterDTO> getPaginatedAccountMastersList(@RequestBody RequestDTO request) {
        return service.getPaginatedAccountMastersList(request);
    }

    @GetMapping("/getAccountTypes")
    public List<AccountTypeGroupDTO> getAccountTypes() {
        return service.getAccountTypes();
    }

    @GetMapping("/getParentAccount/{accountTypeName}")
    public List<DropdownDTO> getParentAccount(@PathVariable String accountTypeName) {
        return service.getParentAccount(accountTypeName);
    }

    @GetMapping("/{id}")
    public AccountMasterDTO getAccountMaster(@PathVariable int id) {
        return service.getAccountMasterById(id);
    }

    @PostMapping()
    public ResponseEntity<ApiResponse<AccountMaster>> createAccountMaster(@RequestBody AccountMasterDTO accountMaster) {
        AccountMaster res = service.saveAccountMaster(accountMaster);
        return ResponseEntity.ok(ResponseUtil.success(res.getId(), "AccountMaster Added successfully"));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<AccountMaster>> updateAccountMaster(@PathVariable int id, @RequestBody AccountMasterDTO accountMaster) {
        AccountMaster res = service.updateAccountMaster(id, accountMaster);
        return ResponseEntity.ok(ResponseUtil.success(res.getId(), "AccountMaster Updated successfully"));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<AccountMaster>> deleteAccountMaster(@PathVariable int id) {
        service.deleteAccountMaster(id);
        return ResponseEntity.ok(ResponseUtil.success(id, "AccountMaster Deleted successfully"));
    }
}
