package com.ttdat.inventoryservice.domain.repositories;

import com.ttdat.inventoryservice.domain.entities.ProductLocation;
import com.ttdat.inventoryservice.domain.entities.RackType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductLocationRepository extends JpaRepository<ProductLocation, String>, JpaSpecificationExecutor<ProductLocation> {
    @Query("SELECT CASE WHEN COUNT(pl) > 0 THEN TRUE ELSE FALSE END FROM ProductLocation pl WHERE pl.warehouse.warehouseId = :warehouseId AND pl.rackName = :rackName AND pl.rackType = :rackType AND pl.rowNumber = :rowNumber AND pl.columnNumber = :columnNumber")
    boolean exists(
            @Param("warehouseId") Long warehouseId,
            @Param("rackName") String rackName,
            @Param("rackType") RackType rackType,
            @Param("rowNumber") Integer rowNumber,
            @Param("columnNumber") Integer columnNumber
    );
}
