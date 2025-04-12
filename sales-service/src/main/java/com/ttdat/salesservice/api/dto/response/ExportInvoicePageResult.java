package com.ttdat.salesservice.api.dto.response;

import com.ttdat.core.api.dto.response.PaginationMeta;
import com.ttdat.salesservice.api.dto.common.ExportInvoiceDTO;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ExportInvoicePageResult {
    PaginationMeta meta;
    List<ExportInvoiceDTO> content;
}
