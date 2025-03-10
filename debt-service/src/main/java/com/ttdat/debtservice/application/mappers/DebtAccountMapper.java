package com.ttdat.debtservice.application.mappers;

import com.ttdat.core.domain.events.DebtAccountCreatedEvent;
import com.ttdat.debtservice.domain.entities.DebtAccount;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface DebtAccountMapper {
    DebtAccount toEntity(DebtAccountCreatedEvent debtAccountCreatedEvent);
}
