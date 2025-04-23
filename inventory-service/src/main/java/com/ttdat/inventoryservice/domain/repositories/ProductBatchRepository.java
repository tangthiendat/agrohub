package com.ttdat.inventoryservice.domain.repositories;

import com.ttdat.inventoryservice.domain.entities.ProductBatch;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductBatchRepository extends JpaRepository<ProductBatch, String>, JpaSpecificationExecutor<ProductBatch> {
    List<ProductBatch> findByProductId(String productId);
    @Query("SELECT pb FROM ProductBatch pb WHERE pb.warehouse.warehouseId = :warehouseId AND pb.productId = :productId")
    List<ProductBatch> findByWarehouseIdAndProductId(Long warehouseId, String productId);
}
