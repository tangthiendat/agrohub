package com.ttdat.debtservice.application.handlers.command;

import com.ttdat.debtservice.application.commands.transaction.CreateDebtTransactionCommand;
import com.ttdat.debtservice.domain.events.transaction.DebtTransactionCreatedEvent;
import lombok.RequiredArgsConstructor;
import org.axonframework.commandhandling.CommandHandler;
import org.axonframework.eventhandling.EventBus;
import org.axonframework.eventhandling.GenericEventMessage;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class DebtTransactionCommandHandler {
    private final EventBus eventBus;

    @CommandHandler
    public void handle(CreateDebtTransactionCommand createDebtTransactionCommand){
        DebtTransactionCreatedEvent debtTransactionCreatedEvent = DebtTransactionCreatedEvent.builder()
                .debtTransactionId(createDebtTransactionCommand.getDebtTransactionId())
                .debtAccountId(createDebtTransactionCommand.getDebtAccountId())
                .paymentMethodId(createDebtTransactionCommand.getPaymentMethodId())
                .amount(createDebtTransactionCommand.getAmount())
                .transactionType(createDebtTransactionCommand.getTransactionType())
                .build();
        eventBus.publish(GenericEventMessage.asEventMessage(debtTransactionCreatedEvent));
    }
}
