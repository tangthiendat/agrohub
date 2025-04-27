package com.ttdat.debtservice.application.handlers.event;

import com.ttdat.debtservice.application.commands.debt.UpdateDebtAccountAmountCommand;
import com.ttdat.debtservice.application.mappers.PaymentMapper;
import com.ttdat.debtservice.domain.repositories.PaymentRepository;
import com.ttdat.debtservice.domain.entities.Payment;
import com.ttdat.debtservice.domain.events.payment.PaymentCreatedEvent;
import lombok.RequiredArgsConstructor;
import org.axonframework.commandhandling.gateway.CommandGateway;
import org.axonframework.config.ProcessingGroup;
import org.axonframework.eventhandling.EventHandler;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;

@Component
@RequiredArgsConstructor
@ProcessingGroup("payment-group")
public class PaymentEventHandler {
    private final PaymentRepository paymentRepository;
    private final PaymentMapper paymentMapper;
    private final CommandGateway commandGateway;

    @Transactional
    @EventHandler
    public void on(PaymentCreatedEvent paymentCreatedEvent) {
        Payment payment = paymentMapper.toEntity(paymentCreatedEvent);
        paymentRepository.save(payment);
        paymentCreatedEvent.getPaymentDetails().forEach(evtPaymentDetail -> {
            if(evtPaymentDetail.getPaymentAmount().compareTo(BigDecimal.ZERO) > 0) {
                UpdateDebtAccountAmountCommand updateDebtAccountAmountCommand = UpdateDebtAccountAmountCommand.builder()
                        .debtAccountId(evtPaymentDetail.getDebtAccountId())
                        .paidAmount(evtPaymentDetail.getPaymentAmount())
                        .transactionSourceId(payment.getPaymentId())
                        .build();
                commandGateway.send(updateDebtAccountAmountCommand);
            }
        });
    }
}
