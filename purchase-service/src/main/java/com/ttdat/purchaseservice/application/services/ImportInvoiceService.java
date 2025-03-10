package com.ttdat.purchaseservice.application.services;

import com.ttdat.purchaseservice.api.dto.request.CreateImportInvoiceRequest;

public interface ImportInvoiceService {
    void createImportInvoice(CreateImportInvoiceRequest request);
}
