package com.ttdat.purchaseservice.domain.events.purchaseorder;

import com.ttdat.purchaseservice.domain.entities.DiscountType;
import com.ttdat.purchaseservice.domain.entities.PurchaseOrderStatus;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class PurchaseOrderCreatedEvent {
    String purchaseOrderId;

    Long warehouseId;

    String supplierId;

    String userId;

    LocalDate orderDate;

    LocalDate expectedDeliveryDate;

    PurchaseOrderStatus status;

    BigDecimal totalAmount;

    BigDecimal discountValue;

    DiscountType discountType;

    BigDecimal vatRate;

    BigDecimal finalAmount;

    String note;
}
