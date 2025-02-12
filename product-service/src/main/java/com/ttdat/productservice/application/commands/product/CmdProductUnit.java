package com.ttdat.productservice.application.commands.product;


import com.fasterxml.jackson.annotation.JsonTypeInfo;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@JsonTypeInfo(use = JsonTypeInfo.Id.CLASS)
public class CmdProductUnit {
    String productUnitId;

    Long unitId;

    Double conversionFactor;

    boolean isDefault;

    List<CmdProductUnitPrice> productUnitPrices;
}
