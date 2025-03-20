package com.ttdat.purchaseservice.api.dto.response;

import com.ttdat.core.api.dto.response.PaginationMeta;
import com.ttdat.purchaseservice.api.dto.common.ImportInvoiceDTO;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ImportInvoicePageResult {
    PaginationMeta meta;
    List<ImportInvoiceDTO> content;
}
