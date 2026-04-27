package com.sandipsky.inventory_system.dto;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class ProductDTO {
    private int id;
    private String name;
    private String code;
    private Double costPrice;
    private Double sellingPrice;
    private Double mrp;
    private int categoryId;
    private String categoryName;
    private int unitId;
    private String unitName;
    private int packingId;
    private String packingName;
    private int taxTypeId;
    private String taxTypeName;
    private double taxRate;
    @JsonProperty("is_active")
    private boolean isActive;
    private boolean isPurchasable;
    private boolean isSellable;
    private boolean isServiceItem;

    public List<String> getProductTypes() {
        List<String> types = new ArrayList<>();
        if (isPurchasable) {
            types.add("Purchasable");
        }
        if (isSellable) {
            types.add("Sellable");
        }
        return types;
    }
}
