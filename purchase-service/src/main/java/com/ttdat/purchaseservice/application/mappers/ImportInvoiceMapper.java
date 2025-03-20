package com.ttdat.purchaseservice.application.mappers;

import com.ttdat.purchaseservice.api.dto.common.ImportInvoiceDTO;
import com.ttdat.purchaseservice.domain.entities.ImportInvoice;
import com.ttdat.purchaseservice.domain.events.importinvoice.ImportInvoiceCreatedEvent;
import org.mapstruct.*;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING,
        injectionStrategy = InjectionStrategy.CONSTRUCTOR,
        uses = {SupplierMapper.class, ImportInvoiceDetailMapper.class})
public interface ImportInvoiceMapper extends EntityMapper<ImportInvoiceDTO, ImportInvoice> {
    @Mapping(target = "supplier.supplierId", source = "supplierId")
    ImportInvoice toEntity(ImportInvoiceCreatedEvent importInvoiceCreatedEvent);

    @AfterMapping
    default void setImportInvoiceIdForDetails(@MappingTarget ImportInvoice importInvoice, ImportInvoiceCreatedEvent importInvoiceCreatedEvent) {
        if(importInvoice.getImportInvoiceDetails() != null) {
            importInvoice.getImportInvoiceDetails().forEach(importInvoiceDetail ->
                    importInvoiceDetail.setImportInvoice(ImportInvoice.builder().importInvoiceId(importInvoice.getImportInvoiceId()).build()));
        }
    }
}
