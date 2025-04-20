package com.ttdat.debtservice.application.handlers.event;

import com.ttdat.debtservice.application.commands.debt.UpdateDebtAccountAmountCommand;
import com.ttdat.debtservice.application.mappers.ReceiptMapper;
import com.ttdat.debtservice.domain.entities.Receipt;
import com.ttdat.debtservice.domain.events.receipt.ReceiptCreatedEvent;
import com.ttdat.debtservice.domain.repositories.ReceiptRepository;
import lombok.RequiredArgsConstructor;
import org.axonframework.commandhandling.gateway.CommandGateway;
import org.axonframework.config.ProcessingGroup;
import org.axonframework.eventhandling.EventHandler;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;

@Component
@RequiredArgsConstructor
@ProcessingGroup("receipt-group")
public class ReceiptEventHandler {
    private final ReceiptRepository receiptRepository;
    private final ReceiptMapper receiptMapper;
    private final CommandGateway commandGateway;

    @Transactional
    @EventHandler
    public void on(ReceiptCreatedEvent receiptCreatedEvent) {
        Receipt receipt = receiptMapper.toEntity(receiptCreatedEvent);
        receiptRepository.save(receipt);
        receiptCreatedEvent.getReceiptDetails().forEach(evtReceiptDetail -> {
            if(evtReceiptDetail.getReceiptAmount().compareTo(BigDecimal.ZERO) > 0){
                UpdateDebtAccountAmountCommand updateDebtAccountAmountCommand = UpdateDebtAccountAmountCommand.builder()
                        .debtAccountId(evtReceiptDetail.getDebtAccountId())
                        .paidAmount(evtReceiptDetail.getReceiptAmount())
                        .transactionSourceId(receipt.getReceiptId())
                        .build();
                commandGateway.send(updateDebtAccountAmountCommand);
            }
        });
    }
}