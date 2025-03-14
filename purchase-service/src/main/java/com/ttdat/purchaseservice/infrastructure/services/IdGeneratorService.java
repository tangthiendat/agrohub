package com.ttdat.purchaseservice.infrastructure.services;

public interface IdGeneratorService {
    String generateSupplierId();
    String generateSupplierProductId();
    String generatePurchaseOrderId();
    String generateImportInvoiceId();
    String generateProductBatchId();
}
