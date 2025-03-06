package com.ttdat.purchaseservice.api.dto.request;

import com.ttdat.purchaseservice.domain.entities.DiscountType;
import com.ttdat.purchaseservice.domain.entities.PurchaseOrderStatus;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UpdatePurchaseOrderRequest {
    Long warehouseId;

    String supplierId;

    String userId;

    LocalDate orderDate;

    LocalDate expectedDeliveryDate;

    PurchaseOrderStatus status;

    List<UpdatePurchaseOrderDetailRequest> purchaseOrderDetails;

    BigDecimal totalAmount;

    BigDecimal discountValue;

    DiscountType discountType;

    BigDecimal vatRate;

    BigDecimal finalAmount;

    String note;
}
