package com.ttdat.customerservice.api.dto.response;

import com.ttdat.core.api.dto.response.PaginationMeta;
import com.ttdat.customerservice.api.dto.CustomerDTO;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class CustomerPageResult {
    PaginationMeta meta;
    List<CustomerDTO> content;
}
