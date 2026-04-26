package com.sandipsky.inventory_system.entity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
@Table(name = "product")
public class Product {
    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    private int id;

    @Column(nullable = false, unique = true)
    private String name;

    private String code;
    
    private boolean isActive;
    
    private boolean isServiceItem = false;
    
    private boolean isPurchasable = true;
    
    private boolean isSellable = true;
    
    private Double costPrice;
    
    private Double sellingPrice;
    
    private Double mrp;

    @ManyToOne
    @JoinColumn(name = "category_id")
    private Category category;

    @ManyToOne
    @JoinColumn(name = "unit_id")
    private Unit unit;

    @ManyToOne
    @JoinColumn(name = "packing_id")
    private Packing packing;

    @ManyToOne
    @JoinColumn(name = "tax_type_id")
    private TaxType taxType;
}