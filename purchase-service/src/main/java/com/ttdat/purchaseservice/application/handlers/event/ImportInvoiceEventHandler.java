package com.ttdat.purchaseservice.application.handlers.event;

import com.ttdat.core.application.commands.debt.CreateDebtAccountCommand;
import com.ttdat.core.application.commands.product.AddProductTotalQuantityCommand;
import com.ttdat.core.application.commands.stock.AddProductStockCommand;
import com.ttdat.core.application.constants.SystemConfigConstants;
import com.ttdat.core.application.queries.product.GetProductStockQuantityQuery;
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
import org.axonframework.messaging.responsetypes.ResponseTypes;
import org.axonframework.queryhandling.QueryGateway;
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
    private final QueryGateway queryGateway;

    @EventHandler
    public void on(ImportInvoiceCreatedEvent importInvoiceCreatedEvent) {
        ImportInvoice importInvoice = importInvoiceMapper.toEntity(importInvoiceCreatedEvent);
        importInvoiceRepository.save(importInvoice);
        importInvoiceCreatedEvent.getImportInvoiceDetails().forEach(importInvoiceDetail -> {
            GetProductStockQuantityQuery getProductStockQuantityQuery = GetProductStockQuantityQuery.builder()
                    .productId(importInvoiceDetail.getProductId())
                    .productUnitId(importInvoiceDetail.getProductUnitId())
                    .quantity(Double.valueOf(importInvoiceDetail.getQuantity()))
                    .build();
            Double productStockQuantity = queryGateway.query(getProductStockQuantityQuery, ResponseTypes.instanceOf(Double.class)).join();
            AddProductStockCommand addProductStockCommand = AddProductStockCommand.builder()
                    .productStockId(UUID.randomUUID().toString())
                    .warehouseId(importInvoiceCreatedEvent.getWarehouseId())
                    .productId(importInvoiceDetail.getProductId())
                    .quantity(Double.valueOf(importInvoiceDetail.getQuantity()))
                    .build();
            commandGateway.send(addProductStockCommand);
            AddProductTotalQuantityCommand addProductTotalQuantityCommand = AddProductTotalQuantityCommand.builder()
                    .productId(importInvoiceDetail.getProductId())
                    .quantity(productStockQuantity)
                    .build();
            commandGateway.send(addProductTotalQuantityCommand);
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
        commandGateway.send(createDebtAccountCommand);
    }
}
