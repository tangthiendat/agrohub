package com.ttdat.debtservice.application.handlers.event;

import com.ttdat.core.application.exceptions.ErrorCode;
import com.ttdat.core.application.exceptions.ResourceNotFoundException;
import com.ttdat.debtservice.domain.entities.DebtTransaction;
import com.ttdat.debtservice.domain.events.debt.DebtAccountCreatedEvent;
import com.ttdat.debtservice.application.commands.transaction.CreateDebtTransactionCommand;
import com.ttdat.debtservice.application.mappers.DebtAccountMapper;
import com.ttdat.debtservice.application.repositories.DebtAccountRepository;
import com.ttdat.debtservice.domain.entities.DebtAccount;
import com.ttdat.debtservice.domain.entities.DebtTransactionType;
import com.ttdat.debtservice.domain.events.debt.DebtAccountAmountUpdatedEvent;
import com.ttdat.debtservice.domain.services.DebtDomainService;
import com.ttdat.debtservice.infrastructure.services.IdGeneratorService;
import lombok.RequiredArgsConstructor;
import org.axonframework.commandhandling.gateway.CommandGateway;
import org.axonframework.config.ProcessingGroup;
import org.axonframework.eventhandling.EventHandler;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;

@Component
@RequiredArgsConstructor
@ProcessingGroup("debt-group")
public class DebtAccountEventHandler {
    private final DebtAccountRepository debtAccountRepository;
    private final DebtAccountMapper debtAccountMapper;
    private final IdGeneratorService idGeneratorService;
    private final DebtDomainService debtDomainService;
    private final CommandGateway commandGateway;

    @Transactional
    @EventHandler
    public void on(DebtAccountCreatedEvent debtAccountCreatedEvent) {
        DebtAccount debtAccount = debtAccountMapper.toEntity(debtAccountCreatedEvent);
        debtAccount.setDebtTransactions(
                List.of(DebtTransaction.builder()
                        .debtTransactionId(idGeneratorService.generateTransactionId())
                        .debtAccount(debtAccount)
                        .transactionType(DebtTransactionType.DEBT)
                        .amount(debtAccount.getTotalAmount())
                        .sourceId(debtAccount.getSourceId())
                        .build())
        );
        debtAccountRepository.save(debtAccount);
//        CreateDebtTransactionCommand createDebtTransactionCommand = CreateDebtTransactionCommand.builder()
//                .debtTransactionId(idGeneratorService.generateTransactionId())
//                .debtAccountId(debtAccount.getDebtAccountId())
//                .transactionType(DebtTransactionType.DEBT)
//                .amount(debtAccount.getTotalAmount())
//                .sourceId(debtAccount.getSourceId())
//                .build();
//        commandGateway.sendAndWait(createDebtTransactionCommand);
    }

    private DebtAccount getDebtAccountById(String debtAccountId) {
        return debtAccountRepository.findById(debtAccountId).orElseThrow(
                () -> new ResourceNotFoundException(ErrorCode.DEBT_ACCOUNT_NOT_FOUND)
        );
    }

    @Transactional
    @EventHandler
    public void on(DebtAccountAmountUpdatedEvent debtAccountAmountUpdatedEvent) {
        DebtAccount debtAccount = getDebtAccountById(debtAccountAmountUpdatedEvent.getDebtAccountId());
        debtAccount.setPaidAmount(debtAccount.getPaidAmount().add(debtAccountAmountUpdatedEvent.getPaidAmount()));
        BigDecimal remainingAmount = debtAccount.getRemainingAmount().subtract(debtAccountAmountUpdatedEvent.getPaidAmount());
        debtAccount.setRemainingAmount(remainingAmount);
        debtAccount.setDebtStatus(debtDomainService.getDebtStatus(remainingAmount, debtAccount.getTotalAmount()));
        debtAccountRepository.save(debtAccount);
        CreateDebtTransactionCommand createDebtTransactionCommand = CreateDebtTransactionCommand.builder()
                .debtTransactionId(idGeneratorService.generateTransactionId())
                .debtAccountId(debtAccount.getDebtAccountId())
                .transactionType(DebtTransactionType.PAYMENT)
                .amount(debtAccountAmountUpdatedEvent.getPaidAmount().negate())
                .sourceId(debtAccountAmountUpdatedEvent.getTransactionSourceId())
                .build();
        commandGateway.send(createDebtTransactionCommand);
    }

}
