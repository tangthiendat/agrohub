package com.ttdat.purchaseservice.application.handlers.event;

import com.ttdat.core.api.dto.response.ProductInfo;
import com.ttdat.core.api.dto.response.ProductUnitInfo;
import com.ttdat.core.api.dto.response.UserInfo;
import com.ttdat.core.api.dto.response.WarehouseInfo;
import com.ttdat.core.application.constants.SystemConfigConstants;
import com.ttdat.core.application.exceptions.ErrorCode;
import com.ttdat.core.application.exceptions.ResourceNotFoundException;
import com.ttdat.core.application.queries.inventory.GetWarehouseInfoByIdQuery;
import com.ttdat.core.application.queries.product.GetProductInfoByIdQuery;
import com.ttdat.core.application.queries.user.GetUserInfoByIdQuery;
import com.ttdat.purchaseservice.api.dto.common.SupplierDTO;
import com.ttdat.purchaseservice.application.mappers.PurchaseOrderMapper;
import com.ttdat.purchaseservice.application.queries.supplier.GetSupplierByIdQuery;
import com.ttdat.purchaseservice.domain.entities.PurchaseOrder;
import com.ttdat.purchaseservice.domain.entities.PurchaseOrderStatus;
import com.ttdat.purchaseservice.domain.events.purchaseorder.PurchaseOrderCancelledEvent;
import com.ttdat.purchaseservice.domain.events.purchaseorder.PurchaseOrderCreatedEvent;
import com.ttdat.purchaseservice.domain.events.purchaseorder.PurchaseOrderStatusUpdatedEvent;
import com.ttdat.purchaseservice.domain.events.purchaseorder.PurchaseOrderUpdatedEvent;
import com.ttdat.purchaseservice.domain.repositories.PurchaseOrderRepository;
import com.ttdat.purchaseservice.infrastructure.kafka.email.EmailPurchaseOrderDetail;
import com.ttdat.core.infrastructure.kafka.email.PurchaseOrderCreatedMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.axonframework.config.ProcessingGroup;
import org.axonframework.eventhandling.EventHandler;
import org.axonframework.messaging.responsetypes.ResponseTypes;
import org.axonframework.queryhandling.QueryGateway;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@Component
@RequiredArgsConstructor
@ProcessingGroup("purchase-order-group")
public class PurchaseOrderEventHandler {
    private final PurchaseOrderRepository purchaseOrderRepository;
    private final PurchaseOrderMapper purchaseOrderMapper;
    private final QueryGateway queryGateway;
    private final KafkaTemplate<String, Object> kafkaTemplate;
    @Value("${kafka.email.topic}")
    private String emailTopic;

    @EventHandler
    public void on(PurchaseOrderCreatedEvent purchaseOrderCreatedEvent) {
        PurchaseOrder purchaseOrder = purchaseOrderMapper.toEntity(purchaseOrderCreatedEvent);
        purchaseOrderRepository.save(purchaseOrder);
        List<EmailPurchaseOrderDetail> purchaseOrderDetails = purchaseOrder.getPurchaseOrderDetails().stream()
                .map(purchaseOrderDetail -> {
                    GetProductInfoByIdQuery getProductInfoByIdQuery = GetProductInfoByIdQuery.builder()
                            .productId(purchaseOrderDetail.getProductId())
                            .build();
                    ProductInfo productInfo = queryGateway.query(getProductInfoByIdQuery, ResponseTypes.instanceOf(ProductInfo.class)).join();
                    ProductUnitInfo currentProductUnit = productInfo.getProductUnits().stream()
                            .filter(productUnitInfo -> productUnitInfo.getProductUnitId().equals(purchaseOrderDetail.getProductUnitId()))
                            .findFirst()
                            .orElseThrow(() -> new ResourceNotFoundException(ErrorCode.PRODUCT_UNIT_NOT_FOUND));
                    return EmailPurchaseOrderDetail.builder()
                            .productName(productInfo.getProductName())
                            .quantity(purchaseOrderDetail.getQuantity())
                            .unitName(currentProductUnit.getUnit().getUnitName())
                            .build();
                })
                .toList();
        GetWarehouseInfoByIdQuery getWarehouseInfoByIdQuery = GetWarehouseInfoByIdQuery.builder()
                .warehouseId(purchaseOrder.getWarehouseId())
                .build();
        WarehouseInfo warehouseInfo = queryGateway.query(getWarehouseInfoByIdQuery, ResponseTypes.instanceOf(WarehouseInfo.class)).join();

        GetSupplierByIdQuery getSupplierByIdQuery = GetSupplierByIdQuery.builder()
                .supplierId(purchaseOrder.getSupplier().getSupplierId())
                .build();
        SupplierDTO supplierDTO = queryGateway.query(getSupplierByIdQuery, ResponseTypes.instanceOf(SupplierDTO.class)).join();

        GetUserInfoByIdQuery getUserInfoByIdQuery = GetUserInfoByIdQuery.builder()
                .userId(purchaseOrderCreatedEvent.getUserId())
                .build();
        UserInfo userInfo = queryGateway.query(getUserInfoByIdQuery, ResponseTypes.instanceOf(UserInfo.class)).join();

        Map<String, Object> context = new HashMap<>();
        context.put("purchaseOrderId", purchaseOrder.getPurchaseOrderId());
        context.put("supplierName", supplierDTO.getSupplierName());
        context.put("orderDate", purchaseOrder.getOrderDate());
        context.put("warehouseName", warehouseInfo.getWarehouseName());
        context.put("warehouseAddress", warehouseInfo.getAddress());
        context.put("purchaseOrderDetails", purchaseOrderDetails);
        context.put("userName", userInfo.getFullName());
        context.put("userEmail", userInfo.getEmail());
        context.put("userPhoneNumber", userInfo.getPhoneNumber());

        PurchaseOrderCreatedMessage purchaseOrderCreatedMessage = PurchaseOrderCreatedMessage.builder()
                .toEmail(supplierDTO.getEmail())
                .subject("ĐƠN ĐẶT HÀNG #" + purchaseOrder.getPurchaseOrderId())
                .templateName(SystemConfigConstants.PURCHASE_ORDER_CREATED_MAIL_TEMPLATE)
                .context(context)
                .build();
        kafkaTemplate.send(emailTopic, purchaseOrderCreatedMessage);
    }

    public PurchaseOrder getPurchaseOrderById(String purchaseOrderId) {
        return purchaseOrderRepository.findById(purchaseOrderId)
                .orElseThrow(() -> new ResourceNotFoundException(ErrorCode.PURCHASE_ORDER_NOT_FOUND));
    }

    @EventHandler
    public void on(PurchaseOrderStatusUpdatedEvent purchaseOrderStatusUpdatedEvent) {
        PurchaseOrder purchaseOrder = getPurchaseOrderById(purchaseOrderStatusUpdatedEvent.getPurchaseOrderId());
        purchaseOrder.setStatus(purchaseOrderStatusUpdatedEvent.getStatus());
        purchaseOrderRepository.save(purchaseOrder);
    }

    @EventHandler
    public void on(PurchaseOrderUpdatedEvent purchaseOrderUpdatedEvent) {
        PurchaseOrder purchaseOrder = getPurchaseOrderById(purchaseOrderUpdatedEvent.getPurchaseOrderId());
        purchaseOrderMapper.updateEntityFromEvent(purchaseOrder, purchaseOrderUpdatedEvent);
        purchaseOrderRepository.save(purchaseOrder);
    }

    @EventHandler
    public void on(PurchaseOrderCancelledEvent purchaseOrderCancelledEvent) {
        PurchaseOrder purchaseOrder = getPurchaseOrderById(purchaseOrderCancelledEvent.getPurchaseOrderId());
        purchaseOrder.setStatus(PurchaseOrderStatus.CANCELLED);
        purchaseOrder.setCancelReason(purchaseOrderCancelledEvent.getCancelReason());
        purchaseOrderRepository.save(purchaseOrder);
    }

}
