package com.ttdat.purchaseservice.api.dto.response;

import com.ttdat.core.api.dto.response.PaginationMeta;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.experimental.SuperBuilder;

import java.util.List;

@Data
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class PurchaseOrderPageResult {
    PaginationMeta meta;
    List<PurchaseOrderListItem> content;
}
