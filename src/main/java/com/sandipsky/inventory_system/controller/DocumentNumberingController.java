package com.sandipsky.inventory_system.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.sandipsky.inventory_system.dto.DocumentNumberingDTO;
import com.sandipsky.inventory_system.service.DocumentNumberingService;

@RestController
@RequestMapping("/documentNumbering")
public class DocumentNumberingController {

    @Autowired
    private DocumentNumberingService service;

    @GetMapping()
    public List<DocumentNumberingDTO> getDocumentNumberings() {
        return service.getDocumentNumberings();
    }

     @GetMapping("/generatePurchaseNumber")
    public String generatePurchaseNumber() {
        return service.generatePurchaseNumber();
    }

    @GetMapping("/generateSalesNumber")
    public String generateSalesNumber() {
        return service.generateSalesNumber();
    }
}
