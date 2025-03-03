package com.ttdat.purchaseservice.domain.aggregate;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.axonframework.modelling.command.EntityId;

import java.math.BigDecimal;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class AggPurchaseOrderDetail {
    @EntityId(routingKey = "purchaseOrderDetailId")
    String purchaseOrderDetailId;

    String purchaseOrderId;

    String productId;

    String productUnitId;

    Integer quantity;

    BigDecimal unitPrice;
}
