package com.ttdat.core.api.dto.response;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class PaginationMeta {
    int page;
    int pageSize;
    long totalElements;
    int totalPages;
}
