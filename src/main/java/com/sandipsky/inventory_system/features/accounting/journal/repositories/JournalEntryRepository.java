package com.sandipsky.inventory_system.journal;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;



public interface JournalEntryRepository extends JpaRepository<JournalEntry, Integer>, JpaSpecificationExecutor<JournalEntry> {
    List<JournalEntry> findByMasterJournalEntryId(Integer masterJournalEntryId);
}
