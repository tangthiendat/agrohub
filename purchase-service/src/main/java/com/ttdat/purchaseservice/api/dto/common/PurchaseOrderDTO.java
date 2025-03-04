package com.ttdat.purchaseservice.api.dto.common;

import com.ttdat.purchaseservice.api.dto.response.PoUserDTO;
import com.ttdat.purchaseservice.api.dto.response.PoWarehouseDTO;
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

    PoUserDTO user;

    PoWarehouseDTO warehouse;

    LocalDate orderDate;

    LocalDate expectedDeliveryDate;

    PurchaseOrderStatus status;

    List<PurchaseOrderDetailDTO> purchaseOrderDetails;

    BigDecimal totalAmount;

    BigDecimal discountValue;

    DiscountType discountType;

    BigDecimal vatRate;

    BigDecimal finalAmount;

    String note;
}
