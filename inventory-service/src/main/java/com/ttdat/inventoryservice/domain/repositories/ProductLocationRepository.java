package com.ttdat.inventoryservice.domain.repositories;

import com.ttdat.inventoryservice.domain.entities.ProductLocation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductLocationRepository extends JpaRepository<ProductLocation, String> {
}
