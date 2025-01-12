package com.ttdat.authservice.api.dto.response;

import com.ttdat.authservice.api.dto.common.PermissionDTO;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class PermissionPageResult {
    PaginationMeta meta;
    List<PermissionDTO> content;
}
