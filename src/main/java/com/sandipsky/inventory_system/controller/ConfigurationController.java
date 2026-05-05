package com.sandipsky.inventory_system.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.sandipsky.inventory_system.dto.ApiResponse;
import com.sandipsky.inventory_system.dto.ConfigurationDTO;
import com.sandipsky.inventory_system.entity.Configuration;
import com.sandipsky.inventory_system.service.ConfigurationService;
import com.sandipsky.inventory_system.util.ResponseUtil;

import java.util.List;

@RestController
@RequestMapping("/configurations")
public class ConfigurationController {

    @Autowired
    private ConfigurationService service;

    @GetMapping()
    public List<ConfigurationDTO> getConfigurations() {
        return service.getConfigurations();
    }

    @PutMapping("/edit")
    public ResponseEntity<ApiResponse<Configuration>> bulkUpdate(@RequestBody List<ConfigurationDTO> configurations) {
        service.bulkUpdate(configurations);
        return ResponseEntity.ok(ResponseUtil.success(0, "Configurations updated successfully"));
    }

    @PutMapping("/edit/{id}")
    public ResponseEntity<ApiResponse<Configuration>> updateConfiguration(@PathVariable int id,
            @RequestBody ConfigurationDTO dto) {
        Configuration res = service.updateConfiguration(id, dto);
        return ResponseEntity.ok(ResponseUtil.success(res.getId(), "Configuration updated successfully"));
    }
}
