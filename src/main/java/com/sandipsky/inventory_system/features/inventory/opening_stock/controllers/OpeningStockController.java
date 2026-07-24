package com.sandipsky.inventory_system.features.inventory.opening_stock.controllers;
import com.sandipsky.inventory_system.features.inventory.opening_stock.services.OpeningStockService;
import com.sandipsky.inventory_system.features.inventory.opening_stock.dtos.OpeningStockDTO;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.sandipsky.inventory_system.common.dto.ApiResponse;
import com.sandipsky.inventory_system.common.dto.filter.RequestDTO;
import com.sandipsky.inventory_system.common.util.ResponseUtil;
import com.sandipsky.inventory_system.features.product.entities.ProductStock;

import org.springframework.data.domain.*;
import org.springframework.http.ResponseEntity;

@RestController
@RequestMapping("/opening-stock")
public class OpeningStockController {

    @Autowired
    private OpeningStockService service;

    @PostMapping("/view")
    public Page<OpeningStockDTO> getPaginatedOpeningStocksList(@RequestBody RequestDTO request) {
        return service.getPaginatedOpeningStocksList(request);
    }

    @PostMapping()
    public ResponseEntity<ApiResponse<ProductStock>> saveOpeningStock(@RequestBody OpeningStockDTO openingStockDTO) {
        ProductStock res = service.saveOpeningStock(openingStockDTO);
        return ResponseEntity.ok(ResponseUtil.success(res.getId(), "Opening Stock Added successfully"));
    }
}
