package com.ttdat.purchaseservice.api.dto.common;

import com.ttdat.core.api.dto.response.UserInfo;
import com.ttdat.core.api.dto.response.WarehouseInfo;
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
public class PurchaseOrderDTO {
    String purchaseOrderId;

    SupplierDTO supplier;

    UserInfo user;

    WarehouseInfo warehouse;

    LocalDate orderDate;

    LocalDate expectedDeliveryDate;

    PurchaseOrderStatus status;

    String note;

    String cancelReason;

    List<PurchaseOrderDetailDTO> purchaseOrderDetails;

    BigDecimal totalAmount;

    BigDecimal discountValue;

    DiscountType discountType;

    BigDecimal vatRate;

    BigDecimal finalAmount;
}
