package com.ttdat.purchaseservice.application.handlers.event;

import com.ttdat.core.application.commands.debt.CreateDebtAccountCommand;
import com.ttdat.core.application.commands.product.UpdateProductTotalQuantityCommand;
import com.ttdat.core.application.commands.stock.AddProductStockCommand;
import com.ttdat.core.application.constants.SystemConfigConstants;
import com.ttdat.core.domain.entities.DebtPartyType;
import com.ttdat.core.domain.entities.DebtSourceType;
import com.ttdat.core.domain.entities.DebtStatus;
import com.ttdat.purchaseservice.application.mappers.ImportInvoiceMapper;
import com.ttdat.purchaseservice.domain.entities.ImportInvoice;
import com.ttdat.purchaseservice.domain.events.importinvoice.ImportInvoiceCreatedEvent;
import com.ttdat.purchaseservice.domain.repositories.ImportInvoiceRepository;
import lombok.RequiredArgsConstructor;
import org.axonframework.commandhandling.gateway.CommandGateway;
import org.axonframework.config.ProcessingGroup;
import org.axonframework.eventhandling.EventHandler;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.UUID;

@Component
@RequiredArgsConstructor
@ProcessingGroup("import-invoice-group")
public class ImportInvoiceEventHandler {
    private final ImportInvoiceRepository importInvoiceRepository;
    private final ImportInvoiceMapper importInvoiceMapper;
    private final CommandGateway commandGateway;

    @EventHandler
    public void on(ImportInvoiceCreatedEvent importInvoiceCreatedEvent) {
        ImportInvoice importInvoice = importInvoiceMapper.toEntity(importInvoiceCreatedEvent);
        importInvoiceRepository.save(importInvoice);
        importInvoiceCreatedEvent.getImportInvoiceDetails().forEach(importInvoiceDetail -> {
            AddProductStockCommand addProductStockCommand = AddProductStockCommand.builder()
                    .productStockId(UUID.randomUUID().toString())
                    .warehouseId(importInvoiceCreatedEvent.getWarehouseId())
                    .productId(importInvoiceDetail.getProductId())
                    .quantity(importInvoiceDetail.getQuantity())
                    .build();
            commandGateway.sendAndWait(addProductStockCommand);
            UpdateProductTotalQuantityCommand updateProductTotalQuantityCommand = UpdateProductTotalQuantityCommand.builder()
                    .productId(importInvoiceDetail.getProductId())
                    .quantity(importInvoiceDetail.getQuantity())
                    .build();
            commandGateway.sendAndWait(updateProductTotalQuantityCommand);
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
    }
}
