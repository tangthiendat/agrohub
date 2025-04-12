package com.ttdat.inventoryservice.domain.repositories;

import com.ttdat.inventoryservice.domain.entities.ProductBatchLocation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductBatchLocationRepository extends JpaRepository<ProductBatchLocation, String> {
}
