package com.ttdat.inventoryservice.domain.repositories;

import com.ttdat.inventoryservice.domain.entities.Warehouse;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface WarehouseRepository extends JpaRepository<Warehouse, Long> {
    boolean existsByWarehouseName(String warehouseName);
}
