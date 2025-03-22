package com.ttdat.debtservice.application.mappers;

import com.ttdat.core.domain.events.DebtAccountCreatedEvent;
import com.ttdat.debtservice.api.dto.response.PartyDebtAccount;
import com.ttdat.debtservice.domain.entities.DebtAccount;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;

import java.util.List;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface DebtAccountMapper {
    DebtAccount toEntity(DebtAccountCreatedEvent debtAccountCreatedEvent);

    PartyDebtAccount toPartyDebtAccount(DebtAccount debtAccount);

    List<PartyDebtAccount> toPartyDebtAccountList(List<DebtAccount> debtAccounts);
}
