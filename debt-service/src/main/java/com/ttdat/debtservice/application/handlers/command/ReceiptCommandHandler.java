package com.ttdat.debtservice.application.handlers.command;

import com.ttdat.debtservice.application.commands.receipt.CreateReceiptCommand;
import com.ttdat.debtservice.domain.events.receipt.EvtReceiptDetail;
import com.ttdat.debtservice.domain.events.receipt.ReceiptCreatedEvent;
import lombok.RequiredArgsConstructor;
import org.axonframework.commandhandling.CommandHandler;
import org.axonframework.eventhandling.EventBus;
import org.axonframework.eventhandling.GenericEventMessage;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class ReceiptCommandHandler {
    private final EventBus eventBus;

    @CommandHandler
    public void handle(CreateReceiptCommand createReceiptCommand) {
        List<EvtReceiptDetail> evtReceiptDetails = createReceiptCommand.getReceiptDetails().stream()
                .map(receiptDetail -> EvtReceiptDetail.builder()
                        .receiptDetailId(receiptDetail.getReceiptDetailId())
                        .debtAccountId(receiptDetail.getDebtAccountId())
                        .receiptAmount(receiptDetail.getReceiptAmount())
                        .build())
                .toList();
        ReceiptCreatedEvent receiptCreatedEvent = ReceiptCreatedEvent.builder()
                .receiptId(createReceiptCommand.getReceiptId())
                .customerId(createReceiptCommand.getCustomerId())
                .warehouseId(createReceiptCommand.getWarehouseId())
                .userId(createReceiptCommand.getUserId())
                .createdDate(createReceiptCommand.getCreatedDate())
                .totalReceivedAmount(createReceiptCommand.getTotalReceivedAmount())
                .paymentMethodId(createReceiptCommand.getPaymentMethodId())
                .note(createReceiptCommand.getNote())
                .receiptDetails(evtReceiptDetails)
                .build();
        eventBus.publish(GenericEventMessage.asEventMessage(receiptCreatedEvent));
    }
}