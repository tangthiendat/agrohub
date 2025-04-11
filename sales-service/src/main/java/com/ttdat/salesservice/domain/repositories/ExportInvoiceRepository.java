package com.ttdat.salesservice.domain.repositories;

import com.ttdat.salesservice.domain.entities.ExportInvoice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ExportInvoiceRepository extends JpaRepository<ExportInvoice, String> {
}
