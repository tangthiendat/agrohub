package com.ttdat.debtservice.application.mappers;

import com.ttdat.core.domain.events.DebtAccountCreatedEvent;
import com.ttdat.debtservice.api.dto.common.DebtAccountDTO;
import com.ttdat.debtservice.api.dto.response.PartyDebtAccount;
import com.ttdat.debtservice.domain.entities.DebtAccount;
import org.mapstruct.InjectionStrategy;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;

import java.util.List;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING,
        injectionStrategy = InjectionStrategy.CONSTRUCTOR,
        uses = {DebtTransactionMapper.class})
public interface DebtAccountMapper extends EntityMapper<DebtAccountDTO, DebtAccount> {
    DebtAccount toEntity(DebtAccountCreatedEvent debtAccountCreatedEvent);

    PartyDebtAccount toPartyDebtAccount(DebtAccount debtAccount);

    List<PartyDebtAccount> toPartyDebtAccountList(List<DebtAccount> debtAccounts);
}
