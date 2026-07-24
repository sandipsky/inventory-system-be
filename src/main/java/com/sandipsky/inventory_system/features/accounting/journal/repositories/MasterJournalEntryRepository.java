package com.sandipsky.inventory_system.journal;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;


public interface MasterJournalEntryRepository
        extends JpaRepository<MasterJournalEntry, Integer>, JpaSpecificationExecutor<MasterJournalEntry> {
    Optional<MasterJournalEntry> findTopByOrderByIdDesc();

    @Query("SELECT m FROM MasterJournalEntry m " +
            "WHERE m.masterPurchaseEntry.id IS NULL AND m.masterSalesEntry.id IS NULL " +
            "ORDER BY m.id DESC")
    Optional<MasterJournalEntry> findTopByOrderByIdDescJournal();

    @Query("""
                SELECT j
                FROM MasterJournalEntry j
                WHERE j.masterPurchaseEntry.id = :masterPurchaseEntryId
            """)
    Optional<MasterJournalEntry> findByMasterPurchaseEntryId(
            @Param("masterPurchaseEntryId") Integer masterPurchaseEntryId);
}
