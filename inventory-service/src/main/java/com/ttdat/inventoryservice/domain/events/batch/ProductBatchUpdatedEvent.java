package com.ttdat.inventoryservice.domain.events.batch;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

import java.time.LocalDate;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = lombok.AccessLevel.PRIVATE)
public class ProductBatchUpdatedEvent {
    String batchId;

    String productId;

    LocalDate manufacturingDate;

    LocalDate expirationDate;

    LocalDate receivedDate;

    Integer quantity;

    List<EvtProductBatchLocation> batchLocations;
}
