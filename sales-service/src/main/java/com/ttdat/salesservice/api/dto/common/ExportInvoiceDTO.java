package com.ttdat.salesservice.api.dto.common;

import com.ttdat.core.api.dto.response.CustomerInfo;
import com.ttdat.core.api.dto.response.UserInfo;
import com.ttdat.core.api.dto.response.WarehouseInfo;
import com.ttdat.core.domain.entities.DiscountType;
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
public class ExportInvoiceDTO {
    String exportInvoiceId;

    CustomerInfo customer;

    WarehouseInfo warehouse;

    UserInfo user;

    LocalDate createdDate;

    String note;

    List<ExportInvoiceDetailDTO> exportInvoiceDetails;

    BigDecimal totalAmount;

    BigDecimal discountValue;

    DiscountType discountType;

    BigDecimal vatRate;

    BigDecimal finalAmount;
}
