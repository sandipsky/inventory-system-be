package com.sandipsky.inventory_system.features.documentnumbering.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("/documentNumbering")
public class DocumentNumberingController {

    @Autowired
    private DocumentNumberingService service;

    @GetMapping()
    public List<DocumentNumberingDTO> getDocumentNumberings() {
        return service.getDocumentNumberings();
    }
}
