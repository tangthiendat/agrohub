package com.ttdat.purchaseservice.application.commands.importinvoice;

import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.ttdat.core.domain.entities.DiscountType;
import lombok.*;
import lombok.experimental.FieldDefaults;
import org.axonframework.modelling.command.TargetAggregateIdentifier;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@JsonTypeInfo(use = JsonTypeInfo.Id.CLASS)
public class CreateImportInvoiceCommand {
    @TargetAggregateIdentifier
    String importInvoiceId;

    Long warehouseId;

    String supplierId;

    String userId;

    LocalDate createdDate;

    String note;

    List<CmdImportInvoiceDetail> importInvoiceDetails;

    BigDecimal totalAmount;

    BigDecimal discountValue;

    DiscountType discountType;

    BigDecimal vatRate;

    BigDecimal finalAmount;

}
