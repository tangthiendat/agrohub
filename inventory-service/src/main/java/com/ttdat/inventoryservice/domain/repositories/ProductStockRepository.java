package com.ttdat.inventoryservice.domain.repositories;

import com.ttdat.inventoryservice.domain.entities.ProductStock;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductStockRepository extends JpaRepository<ProductStock, String>, JpaSpecificationExecutor<ProductStock> {
    @Query("SELECT ps FROM ProductStock ps WHERE ps.warehouse.warehouseId = :warehouseId AND ps.productId = :productId")
    ProductStock findProductStock(@Param("warehouseId") Long warehouseId, @Param("productId") String productId);
}
