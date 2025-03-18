package com.ttdat.inventoryservice.application.commands.batch;

import com.fasterxml.jackson.annotation.JsonTypeInfo;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDate;
import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@JsonTypeInfo(use = JsonTypeInfo.Id.CLASS)
public class UpdateProductBatchCommand {
    String batchId;

    String productId;

    LocalDate manufacturingDate;

    LocalDate expirationDate;

    LocalDate receivedDate;

    Integer quantity;

    List<CmdProductBatchLocation> batchLocations;
}
