package com.ttdat.debtservice.application.handlers.event;

import com.ttdat.core.domain.events.DebtAccountCreatedEvent;
import com.ttdat.debtservice.application.mappers.DebtAccountMapper;
import com.ttdat.debtservice.application.repositories.DebtAccountRepository;
import com.ttdat.debtservice.domain.entities.DebtAccount;
import lombok.RequiredArgsConstructor;
import org.axonframework.config.ProcessingGroup;
import org.axonframework.eventhandling.EventHandler;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@ProcessingGroup("debt-group")
public class DebtAccountEventHandler {
    private final DebtAccountRepository debtAccountRepository;
    private final DebtAccountMapper debtAccountMapper;

    @EventHandler
    public void on(DebtAccountCreatedEvent debtAccountCreatedEvent){
        DebtAccount debtAccount = debtAccountMapper.toEntity(debtAccountCreatedEvent);
        debtAccountRepository.save(debtAccount);
    }

}
