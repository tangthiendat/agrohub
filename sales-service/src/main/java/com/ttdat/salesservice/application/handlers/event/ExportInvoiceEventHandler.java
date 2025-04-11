package com.ttdat.salesservice.application.handlers.event;

import com.ttdat.salesservice.application.mappers.ExportInvoiceMapper;
import com.ttdat.salesservice.domain.entities.ExportInvoice;
import com.ttdat.salesservice.domain.events.exportinvoice.ExportInvoiceCreatedEvent;
import com.ttdat.salesservice.domain.repositories.ExportInvoiceRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.axonframework.config.ProcessingGroup;
import org.axonframework.eventhandling.EventHandler;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Component
@RequiredArgsConstructor
@ProcessingGroup("export-invoice-group")
public class ExportInvoiceEventHandler {
    private final ExportInvoiceRepository exportInvoiceRepository;
    private final ExportInvoiceMapper exportInvoiceMapper;

    @Transactional
    @EventHandler
    public void on(ExportInvoiceCreatedEvent exportInvoiceCreatedEvent){
        ExportInvoice exportInvoice = exportInvoiceMapper.toEntity(exportInvoiceCreatedEvent);
        exportInvoiceRepository.save(exportInvoice);
    }
}
