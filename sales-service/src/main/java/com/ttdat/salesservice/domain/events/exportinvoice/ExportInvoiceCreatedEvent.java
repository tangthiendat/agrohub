package com.ttdat.salesservice.domain.events.exportinvoice;

import com.ttdat.core.domain.entities.DiscountType;
import com.ttdat.salesservice.domain.valueobject.EvtExportInvoiceDetail;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ExportInvoiceCreatedEvent {
    String exportInvoiceId;

    Long warehouseId;

    String customerId;

    String userId;

    LocalDate createdDate;

    String note;

    List<EvtExportInvoiceDetail> exportInvoiceDetails;

    BigDecimal totalAmount;

    BigDecimal discountValue;

    DiscountType discountType;

    BigDecimal vatRate;

    BigDecimal finalAmount;

}
