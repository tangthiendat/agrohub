package com.ttdat.debtservice.application.handlers.command;

import com.ttdat.core.application.commands.debt.CreateDebtAccountCommand;
import com.ttdat.debtservice.application.commands.debt.UpdateDebtAccountAmountCommand;
import com.ttdat.debtservice.domain.events.debt.DebtAccountAmountUpdatedEvent;
import com.ttdat.debtservice.domain.events.debt.DebtAccountCreatedEvent;
import lombok.RequiredArgsConstructor;
import org.axonframework.commandhandling.CommandHandler;
import org.axonframework.eventhandling.EventBus;
import org.axonframework.eventhandling.GenericEventMessage;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class DebtAccountCommandHandler {
    private final EventBus eventBus;

    @CommandHandler
    public void handle(CreateDebtAccountCommand createDebtAccountCommand){
        DebtAccountCreatedEvent debtAccountCreatedEvent = DebtAccountCreatedEvent.builder()
                .debtAccountId(createDebtAccountCommand.getDebtAccountId())
                .partyId(createDebtAccountCommand.getPartyId())
                .partyType(createDebtAccountCommand.getPartyType())
                .sourceId(createDebtAccountCommand.getSourceId())
                .sourceType(createDebtAccountCommand.getSourceType())
                .totalAmount(createDebtAccountCommand.getTotalAmount())
                .paidAmount(createDebtAccountCommand.getPaidAmount())
                .remainingAmount(createDebtAccountCommand.getRemainingAmount())
                .interestRate(createDebtAccountCommand.getInterestRate())
                .dueDate(createDebtAccountCommand.getDueDate())
                .debtStatus(createDebtAccountCommand.getDebtStatus())
                .build();
        eventBus.publish(GenericEventMessage.asEventMessage(debtAccountCreatedEvent));
    }

    @CommandHandler
    public void handle(UpdateDebtAccountAmountCommand updateDebtAccountAmountCommand){
        DebtAccountAmountUpdatedEvent debtAccountAmountUpdatedEvent = DebtAccountAmountUpdatedEvent.builder()
                .debtAccountId(updateDebtAccountAmountCommand.getDebtAccountId())
                .paidAmount(updateDebtAccountAmountCommand.getPaidAmount())
                .transactionSourceId(updateDebtAccountAmountCommand.getTransactionSourceId())
                .build();
        eventBus.publish(GenericEventMessage.asEventMessage(debtAccountAmountUpdatedEvent));
    }
}
