package com.ttdat.purchaseservice.domain.repositories;

import com.ttdat.purchaseservice.domain.entities.ImportInvoice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;

@Repository
public interface ImportInvoiceRepository extends JpaRepository<ImportInvoice, String>, JpaSpecificationExecutor<ImportInvoice> {
    @Query("SELECT COUNT(i) FROM ImportInvoice i WHERE i.warehouseId = :warehouseId AND i.createdDate BETWEEN :startDate AND :endDate")
    Long countByRange(Long warehouseId, LocalDate startDate, LocalDate endDate);
}
