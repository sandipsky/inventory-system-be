package com.sandipsky.inventory_system.journal;

import lombok.Data;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

@Data
public class MasterJournalEntryDTO {
    private int id;

    private String date;

    @JsonProperty("system_entry_no")
    private String systemEntryNo;

    private String remarks;

    private List<JournalEntryDTO> journalEntries;
}
