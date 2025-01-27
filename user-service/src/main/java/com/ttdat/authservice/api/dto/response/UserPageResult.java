package com.ttdat.authservice.api.dto.response;

import com.ttdat.authservice.api.dto.common.UserDTO;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UserPageResult {
    PaginationMeta meta;
    List<UserDTO> content;
}
