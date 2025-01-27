package com.ttdat.authservice.api.dto.response;

import com.ttdat.authservice.api.dto.common.RoleDTO;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class RolePageResult {
    PaginationMeta meta;
    List<RoleDTO> content;
}
