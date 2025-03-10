package com.ttdat.debtservice.domain.aggregate;

import com.ttdat.core.application.commands.debt.CreateDebtAccountCommand;
import com.ttdat.core.domain.entities.DebtPartyType;
import com.ttdat.core.domain.entities.DebtSourceType;
import com.ttdat.core.domain.entities.DebtStatus;
import com.ttdat.core.domain.events.DebtAccountCreatedEvent;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.axonframework.commandhandling.CommandHandler;
import org.axonframework.eventsourcing.EventSourcingHandler;
import org.axonframework.modelling.command.AggregateIdentifier;
import org.axonframework.modelling.command.AggregateLifecycle;
import org.axonframework.spring.stereotype.Aggregate;

import java.math.BigDecimal;
import java.time.LocalDate;

@Aggregate(type = "DebtAggregate")
@FieldDefaults(level = lombok.AccessLevel.PRIVATE)
@RequiredArgsConstructor
public class DebtAggregate {
    @AggregateIdentifier
    String debtAccountId;

    String partyId;

    DebtPartyType partyType;

    String sourceId;

    DebtSourceType sourceType;

    BigDecimal totalAmount;

    BigDecimal paidAmount;

    BigDecimal remainingAmount;

    Double interestRate;

    LocalDate dueDate;

    DebtStatus debtStatus;

    @CommandHandler
    public DebtAggregate(CreateDebtAccountCommand createDebtAccountCommand){
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
        AggregateLifecycle.apply(debtAccountCreatedEvent);
    }

    @EventSourcingHandler
    public void on(DebtAccountCreatedEvent debtAccountCreatedEvent){
        this.debtAccountId = debtAccountCreatedEvent.getDebtAccountId();
        this.partyId = debtAccountCreatedEvent.getPartyId();
        this.partyType = debtAccountCreatedEvent.getPartyType();
        this.sourceId = debtAccountCreatedEvent.getSourceId();
        this.sourceType = debtAccountCreatedEvent.getSourceType();
        this.totalAmount = debtAccountCreatedEvent.getTotalAmount();
        this.paidAmount = debtAccountCreatedEvent.getPaidAmount();
        this.remainingAmount = debtAccountCreatedEvent.getRemainingAmount();
        this.interestRate = debtAccountCreatedEvent.getInterestRate();
        this.dueDate = debtAccountCreatedEvent.getDueDate();
        this.debtStatus = debtAccountCreatedEvent.getDebtStatus();
    }

}
