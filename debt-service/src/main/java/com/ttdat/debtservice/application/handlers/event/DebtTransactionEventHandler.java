package com.ttdat.debtservice.application.handlers.event;

import com.ttdat.debtservice.application.mappers.DebtTransactionMapper;
import com.ttdat.debtservice.application.repositories.DebtTransactionRepository;
import com.ttdat.debtservice.domain.entities.DebtTransaction;
import com.ttdat.debtservice.domain.events.transaction.DebtTransactionCreatedEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.axonframework.config.ProcessingGroup;
import org.axonframework.eventhandling.EventHandler;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@RequiredArgsConstructor
@Slf4j
@ProcessingGroup("debt-transaction-group")
public class DebtTransactionEventHandler {
    private final DebtTransactionRepository debtTransactionRepository;
    private final DebtTransactionMapper debtTransactionMapper;

    @Transactional
    @EventHandler
    public void on(DebtTransactionCreatedEvent debtTransactionCreatedEvent) {
        DebtTransaction debtTransaction = debtTransactionMapper.toEntity(debtTransactionCreatedEvent);
        debtTransactionRepository.save(debtTransaction);
    }
}
