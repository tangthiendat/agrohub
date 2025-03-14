package com.ttdat.purchaseservice.application.services;

import com.ttdat.purchaseservice.api.dto.request.CancelPurchaseOrderRequest;
import com.ttdat.purchaseservice.api.dto.request.CreatePurchaseOrderRequest;
import com.ttdat.purchaseservice.api.dto.request.UpdatePurchaseOrderRequest;
import com.ttdat.purchaseservice.api.dto.request.UpdatePurchaseOrderStatusRequest;

public interface PurchaseOrderService {
    void createPurchaseOrder(CreatePurchaseOrderRequest createPurchaseOrderRequest);

    void updatePurchaseOrderStatus(String id, UpdatePurchaseOrderStatusRequest updatePurchaseOrderStatusRequest);

    void updatePurchaseOrder(String id, UpdatePurchaseOrderRequest updatePurchaseOrderRequest);

    void cancelPurchaseOrder(String id, CancelPurchaseOrderRequest cancelPurchaseOrderRequest);
}
