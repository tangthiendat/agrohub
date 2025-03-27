package com.ttdat.debtservice.application.services.impl;

import com.ttdat.debtservice.api.dto.request.CreatePaymentRequest;
import com.ttdat.debtservice.application.commands.payment.CmdPaymentDetail;
import com.ttdat.debtservice.application.commands.payment.CreatePaymentCommand;
import com.ttdat.debtservice.application.services.PaymentService;
import com.ttdat.debtservice.infrastructure.services.IdGeneratorService;
import lombok.RequiredArgsConstructor;
import org.axonframework.commandhandling.gateway.CommandGateway;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class PaymentServiceImpl implements PaymentService {
    private final CommandGateway commandGateway;
    private final IdGeneratorService idGeneratorService;

    @Override
    public void createPayment(CreatePaymentRequest createPaymentRequest) {
        String paymentId = idGeneratorService.generatePaymentId();
        List<CmdPaymentDetail> cmdPaymentDetails = createPaymentRequest.getPaymentDetails().stream()
                .map(paymentDetail -> CmdPaymentDetail.builder()
                        .paymentDetailId(UUID.randomUUID().toString())
                        .debtAccountId(paymentDetail.getDebtAccountId())
                        .paymentAmount(paymentDetail.getPaymentAmount())
                        .build())
                .toList();
        CreatePaymentCommand createPaymentCommand = CreatePaymentCommand.builder()
                .paymentId(paymentId)
                .supplierId(createPaymentRequest.getSupplierId())
                .warehouseId(createPaymentRequest.getWarehouseId())
                .userId(createPaymentRequest.getUserId())
                .createdDate(createPaymentRequest.getCreatedDate())
                .totalPaidAmount(createPaymentRequest.getTotalPaidAmount())
                .paymentMethodId(createPaymentRequest.getPaymentMethodId())
                .note(createPaymentRequest.getNote())
                .paymentDetails(cmdPaymentDetails)
                .build();
        commandGateway.sendAndWait(createPaymentCommand);
    }
}
