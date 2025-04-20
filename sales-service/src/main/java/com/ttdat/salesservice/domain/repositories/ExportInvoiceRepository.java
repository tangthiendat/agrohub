package com.ttdat.salesservice.domain.repositories;

import com.ttdat.salesservice.domain.entities.ExportInvoice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;

@Repository
public interface ExportInvoiceRepository extends JpaRepository<ExportInvoice, String>, JpaSpecificationExecutor<ExportInvoice> {
    @Query("SELECT COUNT(e) FROM ExportInvoice e WHERE e.warehouseId = :warehouseId AND e.createdDate BETWEEN :startDate AND :endDate")
    Long countByRange(Long warehouseId, LocalDate startDate, LocalDate endDate);
}
