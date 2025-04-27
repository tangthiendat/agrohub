package com.ttdat.core.api.dto.response;

import lombok.*;
import lombok.experimental.FieldDefaults;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ExportSummary {
    String exportInvoiceId;

    LocalDate createdDate;

    BigDecimal totalQuantity;
}
