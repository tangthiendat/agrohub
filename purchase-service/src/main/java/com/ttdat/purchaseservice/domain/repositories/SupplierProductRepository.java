package com.ttdat.purchaseservice.domain.repositories;

import com.ttdat.purchaseservice.domain.entities.SupplierProduct;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SupplierProductRepository extends JpaRepository<SupplierProduct, String> {
}
