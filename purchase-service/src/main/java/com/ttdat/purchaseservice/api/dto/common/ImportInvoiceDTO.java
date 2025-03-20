package com.ttdat.purchaseservice.api.dto.common;

import com.ttdat.core.api.dto.response.UserInfo;
import com.ttdat.core.api.dto.response.WarehouseInfo;
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
public class ImportInvoiceDTO {
    String importInvoiceId;

    SupplierDTO supplier;

    WarehouseInfo warehouse;

    UserInfo user;

    LocalDate createdDate;

    String note;

    List<ImportInvoiceDetailDTO> importInvoiceDetails;

    BigDecimal totalAmount;

    BigDecimal discountValue;

    DiscountType discountType;

    BigDecimal vatRate;

    BigDecimal finalAmount;
}
