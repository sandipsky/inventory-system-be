package com.sandipsky.inventory_system.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
@Table(name = "document_numbering")
public class DocumentNumbering {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(nullable = false)
    private String name;

    @Column(name = "start_date")
    private String startDate;

    @Column(name = "end_date")
    private String endDate;

    @Column(name = "numbering_style")
    private String numberingStyle;

    private String prefix;

    @Column(name = "body_length")
    private int bodyLength;

    @Column(name = "total_length")
    private int totalLength;

    @Column(name = "start_no")
    private int startNo;

    @Column(name = "end_no")
    private int endNo;
}
