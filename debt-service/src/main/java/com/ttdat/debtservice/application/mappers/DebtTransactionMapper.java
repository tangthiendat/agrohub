package com.ttdat.debtservice.application.mappers;

import com.ttdat.debtservice.domain.entities.DebtTransaction;
import com.ttdat.debtservice.domain.events.transaction.DebtTransactionCreatedEvent;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface DebtTransactionMapper {
    @Mapping(target = "debtAccount.debtAccountId", source = "debtAccountId")
    DebtTransaction toEntity(DebtTransactionCreatedEvent debtTransactionCreatedEvent);
}
