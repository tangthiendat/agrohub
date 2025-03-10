package com.ttdat.purchaseservice.application.handlers.event;

import com.ttdat.purchaseservice.application.mappers.ImportInvoiceMapper;
import com.ttdat.purchaseservice.domain.entities.ImportInvoice;
import com.ttdat.purchaseservice.domain.events.importinvoice.ImportInvoiceCreatedEvent;
import com.ttdat.purchaseservice.domain.repositories.ImportInvoiceRepository;
import lombok.RequiredArgsConstructor;
import org.axonframework.config.ProcessingGroup;
import org.axonframework.eventhandling.EventHandler;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@ProcessingGroup("import-invoice-group")
public class ImportInvoiceEventHandler {
    private final ImportInvoiceRepository importInvoiceRepository;
    private final ImportInvoiceMapper importInvoiceMapper;

    @EventHandler
    public void on(ImportInvoiceCreatedEvent importInvoiceCreatedEvent) {
        ImportInvoice importInvoice = importInvoiceMapper.toEntity(importInvoiceCreatedEvent);
        importInvoiceRepository.save(importInvoice);
    }
}
