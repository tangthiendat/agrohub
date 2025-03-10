package com.ttdat.purchaseservice.api.dto.request;

import com.ttdat.purchaseservice.domain.entities.DiscountType;
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
public class CreateImportInvoiceRequest {
    Long warehouseId;

    String supplierId;

    String userId;

    LocalDate createdDate;

    String note;

    List<CreateImportInvoiceDetailRequest> importInvoiceDetails;

    BigDecimal totalAmount;

    BigDecimal discountValue;

    DiscountType discountType;

    BigDecimal vatRate;

    BigDecimal finalAmount;
}
