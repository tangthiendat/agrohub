package com.ttdat.purchaseservice.application.services;

import com.ttdat.purchaseservice.api.dto.request.CreatePurchaseOrderRequest;

public interface PurchaseOrderService {
    void createPurchaseOrder(CreatePurchaseOrderRequest createPurchaseOrderRequest);
}
