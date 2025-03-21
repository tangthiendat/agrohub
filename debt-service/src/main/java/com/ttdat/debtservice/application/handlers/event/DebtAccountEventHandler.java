package com.ttdat.debtservice.application.handlers.event;

import com.ttdat.core.domain.events.DebtAccountCreatedEvent;
import com.ttdat.debtservice.application.commands.transaction.CreateDebtTransactionCommand;
import com.ttdat.debtservice.application.mappers.DebtAccountMapper;
import com.ttdat.debtservice.application.repositories.DebtAccountRepository;
import com.ttdat.debtservice.domain.entities.DebtAccount;
import com.ttdat.debtservice.domain.entities.DebtTransactionType;
import com.ttdat.debtservice.infrastructure.services.IdGeneratorService;
import lombok.RequiredArgsConstructor;
import org.axonframework.commandhandling.gateway.CommandGateway;
import org.axonframework.config.ProcessingGroup;
import org.axonframework.eventhandling.EventHandler;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@RequiredArgsConstructor
@ProcessingGroup("debt-group")
public class DebtAccountEventHandler {
    private final DebtAccountRepository debtAccountRepository;
    private final DebtAccountMapper debtAccountMapper;
    private final IdGeneratorService idGeneratorService;
    private final CommandGateway commandGateway;

    @Transactional
    @EventHandler
    public void on(DebtAccountCreatedEvent debtAccountCreatedEvent){
        DebtAccount debtAccount = debtAccountMapper.toEntity(debtAccountCreatedEvent);
        debtAccountRepository.save(debtAccount);
    }

}
