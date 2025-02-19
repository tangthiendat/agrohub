package com.ttdat.purchaseservice.domain.repositories;

import com.ttdat.purchaseservice.domain.entities.Supplier;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SupplierRepository extends JpaRepository<Supplier, String> {
    boolean existsBySupplierName(String supplierName);
}
