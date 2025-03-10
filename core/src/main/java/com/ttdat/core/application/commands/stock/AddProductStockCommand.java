package com.ttdat.core.application.commands.stock;

import com.fasterxml.jackson.annotation.JsonTypeInfo;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@JsonTypeInfo(use = JsonTypeInfo.Id.CLASS)
public class AddProductStockCommand {
    String productStockId;

    Long warehouseId;

    String productId;

    Integer quantity;
}
