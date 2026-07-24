package com.sandipsky.inventory_system.purchase;
import com.sandipsky.inventory_system.product.Product;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
@Table(name = "purchase_entry")
public class PurchaseEntry {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private int masterPurchaseEntryId;

    private Double quantity;

    private Double costPrice;
   
    private Double sellingPrice;
   
    private Double mrp;

    @ManyToOne
    @JoinColumn(name = "product_id")
    private Product product;
}