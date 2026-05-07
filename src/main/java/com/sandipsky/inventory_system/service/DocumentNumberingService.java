package com.sandipsky.inventory_system.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.sandipsky.inventory_system.dto.DocumentNumberingDTO;
import com.sandipsky.inventory_system.entity.Configuration;
import com.sandipsky.inventory_system.entity.DocumentNumbering;
import com.sandipsky.inventory_system.repository.ConfigurationRepository;
import com.sandipsky.inventory_system.repository.DocumentNumberingRepository;

@Service
public class DocumentNumberingService {

    private static final String FISCAL_YEAR_CONFIG = "fiscal_year";

    @Autowired
    private DocumentNumberingRepository repository;

    @Autowired
    private ConfigurationRepository configurationRepository;

    public List<DocumentNumberingDTO> getDocumentNumberings() {
        String fiscalYear = configurationRepository.findByName(FISCAL_YEAR_CONFIG)
                .map(Configuration::getValue)
                .orElse("");

        return repository.findAll().stream()
                .map(entity -> mapToDTO(entity, fiscalYear))
                .collect(Collectors.toList());
    }

    private DocumentNumberingDTO mapToDTO(DocumentNumbering entity, String fiscalYear) {
        DocumentNumberingDTO dto = new DocumentNumberingDTO();
        dto.setId(entity.getId());
        dto.setName(entity.getName());
        dto.setStartDate(prepend(fiscalYear, entity.getStartDate()));
        dto.setEndDate(prepend(fiscalYear, entity.getEndDate()));
        dto.setNumberingStyle(entity.getNumberingStyle());
        dto.setPrefix(prepend(fiscalYear, entity.getPrefix()));
        dto.setBodyLength(entity.getBodyLength());
        dto.setTotalLength(entity.getTotalLength());
        dto.setStartNo(entity.getStartNo());
        dto.setEndNo(entity.getEndNo());
        return dto;
    }

    private String prepend(String fiscalYear, String value) {
        if (value == null) {
            return fiscalYear;
        }
        return fiscalYear + value;
    }
}
