package com.sandipsky.inventory_system.product;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface ProductStockRepository
        extends JpaRepository<ProductStock, Integer>, JpaSpecificationExecutor<ProductStock> {

    ProductStock findByProductId(Integer productId);
}
