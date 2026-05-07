package com.sandipsky.inventory_system.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class DocumentNumberingDTO {

    private int id;

    private String name;

    @JsonProperty("start_date")
    private String startDate;

    @JsonProperty("end_date")
    private String endDate;

    @JsonProperty("numbering_style")
    private String numberingStyle;

    private String prefix;

    @JsonProperty("body_length")
    private int bodyLength;

    @JsonProperty("total_length")
    private int totalLength;

    @JsonProperty("start_no")
    private int startNo;

    @JsonProperty("end_no")
    private int endNo;
}
