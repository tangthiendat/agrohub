package com.ttdat.core.api.dto.response;

import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDate;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ProductBatchInfo {
    String batchId;

    LocalDate manufacturingDate;

    LocalDate expirationDate;

    LocalDate receivedDate;

    Double quantity;
}
