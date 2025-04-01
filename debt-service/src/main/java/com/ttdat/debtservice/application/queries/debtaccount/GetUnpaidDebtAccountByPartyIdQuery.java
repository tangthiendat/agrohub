package com.ttdat.debtservice.application.queries.debtaccount;

import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.ttdat.core.domain.entities.DebtPartyType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = lombok.AccessLevel.PRIVATE)
@JsonTypeInfo(use = JsonTypeInfo.Id.CLASS)
public class GetUnpaidDebtAccountByPartyIdQuery {
    String partyId;
    DebtPartyType partyType;
}
