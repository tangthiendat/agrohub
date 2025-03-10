package com.ttdat.purchaseservice.application.sagas;

import com.ttdat.core.application.commands.debt.CreateDebtAccountCommand;
import com.ttdat.core.application.commands.stock.AddProductStockCommand;
import com.ttdat.core.application.constants.SystemConfigConstants;
import com.ttdat.core.domain.entities.DebtPartyType;
import com.ttdat.core.domain.entities.DebtSourceType;
import com.ttdat.core.domain.entities.DebtStatus;
import com.ttdat.purchaseservice.domain.events.importinvoice.ImportInvoiceCreatedEvent;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.axonframework.commandhandling.gateway.CommandGateway;
import org.axonframework.modelling.saga.SagaEventHandler;
import org.axonframework.modelling.saga.SagaLifecycle;
import org.axonframework.modelling.saga.StartSaga;
import org.axonframework.spring.stereotype.Saga;
import org.springframework.beans.factory.annotation.Autowired;

import java.math.BigDecimal;
import java.util.UUID;

@Saga
@Slf4j
@NoArgsConstructor(force = true)
public class ImportInvoiceSaga {
    @Autowired
    private final transient CommandGateway commandGateway;

    @StartSaga
    @SagaEventHandler(associationProperty = "importInvoiceId")
    public void on(ImportInvoiceCreatedEvent importInvoiceCreatedEvent){
        importInvoiceCreatedEvent.getImportInvoiceDetails().forEach(importInvoiceDetail -> {
            AddProductStockCommand addProductStockCommand = AddProductStockCommand.builder()
                    .productStockId(UUID.randomUUID().toString())
                    .warehouseId(importInvoiceCreatedEvent.getWarehouseId())
                    .productId(importInvoiceDetail.getProductId())
                    .quantity(importInvoiceDetail.getQuantity())
                    .build();
            commandGateway.sendAndWait(addProductStockCommand);
        });

        CreateDebtAccountCommand createDebtAccountCommand = CreateDebtAccountCommand.builder()
                .debtAccountId(UUID.randomUUID().toString())
                .partyId(importInvoiceCreatedEvent.getSupplierId())
                .partyType(DebtPartyType.SUPPLIER)
                .sourceId(importInvoiceCreatedEvent.getImportInvoiceId())
                .sourceType(DebtSourceType.IMPORT_INVOICE)
                .totalAmount(importInvoiceCreatedEvent.getFinalAmount())
                .paidAmount(BigDecimal.ZERO)
                .remainingAmount(importInvoiceCreatedEvent.getFinalAmount())
                .interestRate(SystemConfigConstants.OVERDUE_INTEREST_RATE)
                .dueDate(importInvoiceCreatedEvent.getCreatedDate().plusDays(SystemConfigConstants.OVERDUE_DEBT_DAYS))
                .debtStatus(DebtStatus.UNPAID)
                .build();
        commandGateway.sendAndWait(createDebtAccountCommand);
        SagaLifecycle.end();
    }

}
