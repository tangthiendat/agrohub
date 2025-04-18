package com.ttdat.purchaseservice.application.services.impl;

import com.ttdat.purchaseservice.api.dto.request.CancelPurchaseOrderRequest;
import com.ttdat.purchaseservice.api.dto.request.CreatePurchaseOrderRequest;
import com.ttdat.purchaseservice.api.dto.request.UpdatePurchaseOrderRequest;
import com.ttdat.purchaseservice.api.dto.request.UpdatePurchaseOrderStatusRequest;
import com.ttdat.purchaseservice.application.commands.purchaseorder.*;
import com.ttdat.purchaseservice.application.services.PurchaseOrderService;
import com.ttdat.purchaseservice.infrastructure.services.IdGeneratorService;
import lombok.RequiredArgsConstructor;
import org.axonframework.commandhandling.gateway.CommandGateway;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class PurchaseOrderServiceImpl implements PurchaseOrderService {
    private final CommandGateway commandGateway;
    private final IdGeneratorService idGeneratorService;

    @Override
    public void createPurchaseOrder(CreatePurchaseOrderRequest createPurchaseOrderRequest) {
        String purchaseOrderId = idGeneratorService.generatePurchaseOrderId();
        List<CmdPurchaseOrderDetail> cmdPurchaseOrderDetails = createPurchaseOrderRequest.getPurchaseOrderDetails().stream()
                .map(purchaseOrderDetail -> CmdPurchaseOrderDetail.builder()
                        .purchaseOrderDetailId(UUID.randomUUID().toString())
                        .productId(purchaseOrderDetail.getProductId())
                        .productUnitId(purchaseOrderDetail.getProductUnitId())
                        .quantity(purchaseOrderDetail.getQuantity())
                        .build())
                .toList();
        CreatePurchaseOrderCommand createPurchaseOrderCommand = CreatePurchaseOrderCommand.builder()
                .purchaseOrderId(purchaseOrderId)
                .warehouseId(createPurchaseOrderRequest.getWarehouseId())
                .supplierId(createPurchaseOrderRequest.getSupplierId())
                .userId(createPurchaseOrderRequest.getUserId())
                .orderDate(createPurchaseOrderRequest.getOrderDate())
//                .expectedDeliveryDate(createPurchaseOrderRequest.getExpectedDeliveryDate())
                .status(createPurchaseOrderRequest.getStatus())
                .purchaseOrderDetails(cmdPurchaseOrderDetails)
                .totalAmount(createPurchaseOrderRequest.getTotalAmount())
                .discountValue(createPurchaseOrderRequest.getDiscountValue())
                .discountType(createPurchaseOrderRequest.getDiscountType())
                .vatRate(createPurchaseOrderRequest.getVatRate())
                .finalAmount(createPurchaseOrderRequest.getFinalAmount())
                .note(createPurchaseOrderRequest.getNote())
                .build();
        commandGateway.sendAndWait(createPurchaseOrderCommand);
    }

    @Override
    public void updatePurchaseOrderStatus(String id, UpdatePurchaseOrderStatusRequest updatePurchaseOrderStatusRequest) {
        UpdatePurchaseOrderStatusCommand updatePurchaseOrderStatusCommand = UpdatePurchaseOrderStatusCommand.builder()
                .purchaseOrderId(id)
                .status(updatePurchaseOrderStatusRequest.getStatus())
                .build();
        commandGateway.sendAndWait(updatePurchaseOrderStatusCommand);
    }

    @Override
    public void updatePurchaseOrder(String id, UpdatePurchaseOrderRequest updatePurchaseOrderRequest) {
        List<CmdPurchaseOrderDetail> cmdPurchaseOrderDetails = updatePurchaseOrderRequest.getPurchaseOrderDetails().stream()
                .map(purchaseOrderDetail -> CmdPurchaseOrderDetail.builder()
                        .purchaseOrderDetailId(UUID.randomUUID().toString())
                        .productId(purchaseOrderDetail.getProductId())
                        .productUnitId(purchaseOrderDetail.getProductUnitId())
                        .quantity(purchaseOrderDetail.getQuantity())
                        .unitPrice(purchaseOrderDetail.getUnitPrice())
                        .build())
                .toList();
        UpdatePurchaseOrderCommand updatePurchaseOrderCommand = UpdatePurchaseOrderCommand.builder()
                .purchaseOrderId(id)
                .warehouseId(updatePurchaseOrderRequest.getWarehouseId())
                .supplierId(updatePurchaseOrderRequest.getSupplierId())
                .userId(updatePurchaseOrderRequest.getUserId())
                .orderDate(updatePurchaseOrderRequest.getOrderDate())
                .expectedDeliveryDate(updatePurchaseOrderRequest.getExpectedDeliveryDate())
                .status(updatePurchaseOrderRequest.getStatus())
                .purchaseOrderDetails(cmdPurchaseOrderDetails)
                .totalAmount(updatePurchaseOrderRequest.getTotalAmount())
                .discountValue(updatePurchaseOrderRequest.getDiscountValue())
                .discountType(updatePurchaseOrderRequest.getDiscountType())
                .vatRate(updatePurchaseOrderRequest.getVatRate())
                .finalAmount(updatePurchaseOrderRequest.getFinalAmount())
                .note(updatePurchaseOrderRequest.getNote())
                .build();
        commandGateway.sendAndWait(updatePurchaseOrderCommand);
    }

    @Override
    public void cancelPurchaseOrder(String id, CancelPurchaseOrderRequest cancelPurchaseOrderRequest) {
        CancelPurchaseOrderCommand cancelPurchaseOrderCommand = CancelPurchaseOrderCommand.builder()
                .purchaseOrderId(id)
                .cancelReason(cancelPurchaseOrderRequest.getCancelReason())
                .build();
        commandGateway.sendAndWait(cancelPurchaseOrderCommand);
    }
}
