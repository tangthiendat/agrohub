package com.ttdat.salesservice.api.dto.common;

import com.ttdat.core.api.dto.response.ProductInfo;
import com.ttdat.core.api.dto.response.ProductUnitInfo;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.math.BigDecimal;
import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ExportInvoiceDetailDTO {
    String exportInvoiceDetailId;

    ProductInfo product;

    ProductUnitInfo productUnit;

    Integer quantity;

    BigDecimal unitPrice;

    List<ExportInvoiceDetailBatchDTO> detailBatches;
}
