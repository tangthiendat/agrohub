package com.ttdat.salesservice.application.mappers;

import com.ttdat.salesservice.api.dto.common.ExportInvoiceDTO;
import com.ttdat.salesservice.domain.entities.ExportInvoice;
import com.ttdat.salesservice.domain.events.exportinvoice.ExportInvoiceCreatedEvent;
import org.mapstruct.*;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING,
        injectionStrategy = InjectionStrategy.CONSTRUCTOR,
        uses = {ExportInvoiceDetailMapper.class})
public interface ExportInvoiceMapper extends EntityMapper<ExportInvoiceDTO, ExportInvoice> {
    ExportInvoice toEntity(ExportInvoiceCreatedEvent exportInvoiceCreatedEvent);

    @AfterMapping
    default void setExportInvoiceDetailForDetails(@MappingTarget ExportInvoice exportInvoice, ExportInvoiceCreatedEvent exportInvoiceCreatedEvent) {
        if (exportInvoice.getExportInvoiceDetails() != null) {
            exportInvoice.getExportInvoiceDetails().forEach(exportInvoiceDetail ->
                    exportInvoiceDetail.setExportInvoice(
                            ExportInvoice.builder()
                                    .exportInvoiceId(exportInvoice.getExportInvoiceId())
                                    .build()));
        }
    }
}
