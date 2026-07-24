package com.sandipsky.inventory_system.features.documentnumbering.services;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.sandipsky.inventory_system.journal.MasterJournalEntry;
import com.sandipsky.inventory_system.purchase.MasterPurchaseEntry;
import com.sandipsky.inventory_system.sales.MasterSalesEntry;
import com.sandipsky.inventory_system.journal.MasterJournalEntryRepository;
import com.sandipsky.inventory_system.purchase.MasterPurchaseEntryRepository;
import com.sandipsky.inventory_system.sales.MasterSalesEntryRepository;

@Service
public class DocumentNumberingService {

    @Autowired
    private DocumentNumberingRepository repository;

    @Autowired
    private MasterPurchaseEntryRepository masterPurchaseEntryRepository;

    @Autowired
    private MasterSalesEntryRepository masterSalesEntryRepository;

    @Autowired
    private MasterJournalEntryRepository masterJournalEntryRepository;

    public List<DocumentNumberingDTO> getDocumentNumberings() {
        return repository.findAll().stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    public String generatePurchaseNumber() {
        DocumentNumbering pref = repository.findByName("Purchase Entry")
                .orElseThrow(() -> new RuntimeException("Document numbering not found for Purchase Entry"));

        Optional<MasterPurchaseEntry> lastEntryOpt = masterPurchaseEntryRepository.findTopByOrderByIdDesc();
        return buildNextNumber(pref, lastEntryOpt.map(MasterPurchaseEntry::getSystemEntryNo).orElse(null));
    }

    public String generateSalesNumber() {
        DocumentNumbering pref = repository.findByName("Sales Entry")
                .orElseThrow(() -> new RuntimeException("Document numbering not found for Sales Entry"));

        Optional<MasterSalesEntry> lastEntryOpt = masterSalesEntryRepository.findTopByOrderByIdDesc();
        return buildNextNumber(pref, lastEntryOpt.map(MasterSalesEntry::getSystemEntryNo).orElse(null));
    }

    public String generateJournalNumber() {
        DocumentNumbering pref = repository.findByName("Journal Entry")
                .orElseThrow(() -> new RuntimeException("Document numbering not found for Journal Entry"));

        Optional<MasterJournalEntry> lastEntryOpt = masterJournalEntryRepository.findTopByOrderByIdDescJournal();
        return buildNextNumber(pref, lastEntryOpt.map(MasterJournalEntry::getSystemEntryNo).orElse(null));
    }

    private String buildNextNumber(DocumentNumbering pref, String lastNumber) {
        int nextNumber = pref.getStartNo();

        if (lastNumber != null && pref.getPrefix() != null) {
            String numericPart = lastNumber.replace(pref.getPrefix(), "");
            try {
                nextNumber = Integer.parseInt(numericPart) + 1;
            } catch (NumberFormatException ignored) {
            }
        }

        if (nextNumber > pref.getEndNo()) {
            throw new IllegalStateException("Document number has exceeded the configured end number.");
        }

        String formattedNumber = String.format("%0" + pref.getBodyLength() + "d", nextNumber);
        return (pref.getPrefix() == null ? "" : pref.getPrefix()) + formattedNumber;
    }

    private DocumentNumberingDTO mapToDTO(DocumentNumbering entity) {
        DocumentNumberingDTO dto = new DocumentNumberingDTO();
        dto.setId(entity.getId());
        dto.setName(entity.getName());
        dto.setPrefix(entity.getPrefix());
        dto.setBodyLength(entity.getBodyLength());
        dto.setStartNo(entity.getStartNo());
        dto.setEndNo(entity.getEndNo());
        return dto;
    }
}
