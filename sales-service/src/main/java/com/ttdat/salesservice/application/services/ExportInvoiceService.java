package com.ttdat.salesservice.application.services;

import com.ttdat.salesservice.api.dto.request.CreateExportInvoiceRequest;

public interface ExportInvoiceService {
    void createExportInvoice(CreateExportInvoiceRequest createExportInvoiceRequest);
}
