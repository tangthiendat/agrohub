package com.ttdat.debtservice.application.handlers.command;

import com.ttdat.debtservice.application.commands.payment.CreatePaymentCommand;
import com.ttdat.debtservice.domain.events.payment.EvtPaymentDetail;
import com.ttdat.debtservice.domain.events.payment.PaymentCreatedEvent;
import lombok.RequiredArgsConstructor;
import org.axonframework.commandhandling.CommandHandler;
import org.axonframework.eventhandling.EventBus;
import org.axonframework.eventhandling.GenericEventMessage;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class PaymentCommandHandler {
    private final EventBus eventBus;

    @CommandHandler
    public void handle (CreatePaymentCommand createPaymentCommand){
        List<EvtPaymentDetail> evtPaymentDetails = createPaymentCommand.getPaymentDetails().stream()
                .map(paymentDetail -> EvtPaymentDetail.builder()
                        .paymentDetailId(paymentDetail.getPaymentDetailId())
                        .debtAccountId(paymentDetail.getDebtAccountId())
                        .paymentAmount(paymentDetail.getPaymentAmount())
                        .build())
                .toList();
        PaymentCreatedEvent paymentCreatedEvent = PaymentCreatedEvent.builder()
                .paymentId(createPaymentCommand.getPaymentId())
                .supplierId(createPaymentCommand.getSupplierId())
                .warehouseId(createPaymentCommand.getWarehouseId())
                .userId(createPaymentCommand.getUserId())
                .createdDate(createPaymentCommand.getCreatedDate())
                .totalPaidAmount(createPaymentCommand.getTotalPaidAmount())
                .paymentMethodId(createPaymentCommand.getPaymentMethodId())
                .note(createPaymentCommand.getNote())
                .paymentDetails(evtPaymentDetails)
                .build();
        eventBus.publish(GenericEventMessage.asEventMessage(paymentCreatedEvent));
    }
}
