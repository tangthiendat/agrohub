package com.ttdat.debtservice.application.services.impl;

import com.ttdat.debtservice.api.dto.request.CreateReceiptRequest;
import com.ttdat.debtservice.application.commands.receipt.CmdReceiptDetail;
import com.ttdat.debtservice.application.commands.receipt.CreateReceiptCommand;
import com.ttdat.debtservice.application.services.ReceiptService;
import com.ttdat.debtservice.infrastructure.services.IdGeneratorService;
import lombok.RequiredArgsConstructor;
import org.axonframework.commandhandling.gateway.CommandGateway;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ReceiptServiceImpl implements ReceiptService {
    private final CommandGateway commandGateway;
    private final IdGeneratorService idGeneratorService;

    @Override
    public void createReceipt(CreateReceiptRequest createReceiptRequest) {
        String receiptId = idGeneratorService.generateReceiptId();
        List<CmdReceiptDetail> cmdReceiptDetails = createReceiptRequest.getReceiptDetails().stream()
                .map(receiptDetail -> CmdReceiptDetail.builder()
                        .receiptDetailId(UUID.randomUUID().toString())
                        .debtAccountId(receiptDetail.getDebtAccountId())
                        .receiptAmount(receiptDetail.getReceiptAmount())
                        .build())
                .toList();
        CreateReceiptCommand createReceiptCommand = CreateReceiptCommand.builder()
                .receiptId(receiptId)
                .customerId(createReceiptRequest.getCustomerId())
                .warehouseId(createReceiptRequest.getWarehouseId())
                .userId(createReceiptRequest.getUserId())
                .createdDate(createReceiptRequest.getCreatedDate())
                .totalReceivedAmount(createReceiptRequest.getTotalReceivedAmount())
                .paymentMethodId(createReceiptRequest.getPaymentMethodId())
                .note(createReceiptRequest.getNote())
                .receiptDetails(cmdReceiptDetails)
                .build();
        commandGateway.sendAndWait(createReceiptCommand);
    }
}