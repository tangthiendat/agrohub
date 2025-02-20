package com.ttdat.purchaseservice.domain.repositories;

import com.ttdat.purchaseservice.domain.entities.Supplier;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SupplierRepository extends JpaRepository<Supplier, String>, JpaSpecificationExecutor<Supplier> {
    boolean existsBySupplierName(String supplierName);

    @Query("SELECT s FROM Supplier s JOIN s.supplierProducts p WHERE p.productId = :productId")
    List<Supplier> findByProductId(String productId);
}
