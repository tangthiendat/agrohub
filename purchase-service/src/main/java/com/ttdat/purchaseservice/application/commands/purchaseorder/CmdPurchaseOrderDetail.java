package com.ttdat.purchaseservice.application.commands.purchaseorder;

import com.fasterxml.jackson.annotation.JsonTypeInfo;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.math.BigDecimal;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@JsonTypeInfo(use = JsonTypeInfo.Id.CLASS)
public class CmdPurchaseOrderDetail {
    String purchaseOrderDetailId;

    String productId;

    String productUnitId;

    Integer quantity;

    BigDecimal unitPrice;
}
