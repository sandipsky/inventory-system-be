package com.sandipsky.inventory_system.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.sandipsky.inventory_system.dto.ApiResponse;
import com.sandipsky.inventory_system.dto.RoleDTO;
import com.sandipsky.inventory_system.dto.RoleOperationGroupDTO;
import com.sandipsky.inventory_system.dto.filter.RequestDTO;
import com.sandipsky.inventory_system.entity.Role;
import com.sandipsky.inventory_system.security.RequiresOperation;
import com.sandipsky.inventory_system.service.RoleService;
import com.sandipsky.inventory_system.util.ResponseUtil;

@RestController
@RequestMapping("/roles")
public class RoleController {

    @Autowired
    private RoleService service;

    @GetMapping()
    @RequiresOperation("ViewRole")
    public List<RoleDTO> getRoles() {
        return service.getRoles();
    }

    @PostMapping("/view")
    @RequiresOperation("ViewRole")
    public Page<RoleDTO> getPaginatedRolesList(@RequestBody RequestDTO request) {
        return service.getPaginatedRolesList(request);
    }

    @GetMapping("/{id}")
    @RequiresOperation("ViewRole")
    public RoleDTO getRole(@PathVariable int id) {
        return service.getRoleById(id);
    }

    @GetMapping("/operations/{id}")
    @RequiresOperation("ViewRole")
    public List<RoleOperationGroupDTO> getAllRoleOperations(@PathVariable int id) {
        return service.getAllRoleOperations(id);
    }

    @PostMapping()
    @RequiresOperation("CreateRole")
    public ResponseEntity<ApiResponse<Role>> createRole(@RequestBody RoleDTO role) {
        Role res = service.saveRole(role);
        return ResponseEntity.ok(ResponseUtil.success(res.getId(), "Role Added successfully"));
    }

    @PutMapping("/{id}")
    @RequiresOperation("EditRole")
    public ResponseEntity<ApiResponse<Role>> updateRole(@PathVariable int id, @RequestBody RoleDTO role) {
        Role res = service.updateRole(id, role);
        return ResponseEntity.ok(ResponseUtil.success(res.getId(), "Role Updated successfully"));
    }

    @DeleteMapping("/{id}")
    @RequiresOperation("DeleteRole")
    public ResponseEntity<ApiResponse<Role>> deleteRole(@PathVariable int id) {
        service.deleteRole(id);
        return ResponseEntity.ok(ResponseUtil.success(id, "Role Deleted successfully"));
    }
}
