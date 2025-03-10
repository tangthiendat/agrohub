package com.ttdat.core.application.commands.batch;

import com.fasterxml.jackson.annotation.JsonTypeInfo;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDate;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@JsonTypeInfo(use = JsonTypeInfo.Id.CLASS)
public class CreateProductBatchCommand {
    String batchId;

    Long warehouseId;

    String productId;

    String importInvoiceDetailId;

    LocalDate manufacturingDate;

    LocalDate expirationDate;

    LocalDate receivedDate;

    Integer quantity;
}
