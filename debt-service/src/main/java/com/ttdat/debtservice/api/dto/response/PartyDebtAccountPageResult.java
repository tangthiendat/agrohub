package com.ttdat.debtservice.api.dto.response;

import com.ttdat.core.api.dto.response.PaginationMeta;
import com.ttdat.debtservice.api.dto.common.DebtAccountDTO;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class PartyDebtAccountPageResult {
    PaginationMeta meta;
    List<DebtAccountDTO> content;
}
