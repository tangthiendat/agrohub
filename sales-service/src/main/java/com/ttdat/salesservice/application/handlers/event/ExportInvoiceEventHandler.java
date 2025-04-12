package com.ttdat.salesservice.application.handlers.event;

import com.ttdat.core.application.commands.batch.ReduceProductBatchQuantityCommand;
import com.ttdat.core.application.commands.debt.CreateDebtAccountCommand;
import com.ttdat.core.application.commands.location.ReduceProductBatchLocationQuantityCommand;
import com.ttdat.core.application.commands.product.ReduceProductTotalQuantityCommand;
import com.ttdat.core.application.commands.stock.ReduceProductStockCommand;
import com.ttdat.core.application.constants.SystemConfigConstants;
import com.ttdat.core.application.queries.product.GetProductStockQuantityQuery;
import com.ttdat.core.domain.entities.DebtPartyType;
import com.ttdat.core.domain.entities.DebtSourceType;
import com.ttdat.core.domain.entities.DebtStatus;
import com.ttdat.salesservice.application.mappers.ExportInvoiceMapper;
import com.ttdat.salesservice.domain.entities.ExportInvoice;
import com.ttdat.salesservice.domain.events.exportinvoice.ExportInvoiceCreatedEvent;
import com.ttdat.salesservice.domain.repositories.ExportInvoiceRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.axonframework.commandhandling.gateway.CommandGateway;
import org.axonframework.config.ProcessingGroup;
import org.axonframework.eventhandling.EventHandler;
import org.axonframework.messaging.responsetypes.ResponseTypes;
import org.axonframework.queryhandling.QueryGateway;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.UUID;

@Slf4j
@Component
@RequiredArgsConstructor
@ProcessingGroup("export-invoice-group")
public class ExportInvoiceEventHandler {
    private final ExportInvoiceRepository exportInvoiceRepository;
    private final ExportInvoiceMapper exportInvoiceMapper;
    private final CommandGateway commandGateway;
    private final QueryGateway queryGateway;

    @Transactional
    @EventHandler
    public void on(ExportInvoiceCreatedEvent exportInvoiceCreatedEvent){
        ExportInvoice exportInvoice = exportInvoiceMapper.toEntity(exportInvoiceCreatedEvent);
        exportInvoiceRepository.save(exportInvoice);
        exportInvoiceCreatedEvent.getExportInvoiceDetails().forEach(evtExportInvoiceDetail -> {
            evtExportInvoiceDetail.getDetailBatches().forEach(evtExportInvoiceDetailBatch -> {
                evtExportInvoiceDetailBatch.getDetailBatchLocations().forEach(evtExportInvoiceDetailBatchLocation -> {
                    ReduceProductBatchLocationQuantityCommand reduceProductBatchLocationQuantityCommand = ReduceProductBatchLocationQuantityCommand.builder()
                            .batchLocationId(evtExportInvoiceDetailBatchLocation.getBatchLocationId())
                            .quantity(evtExportInvoiceDetailBatchLocation.getQuantity())
                            .build();
                    commandGateway.sendAndWait(reduceProductBatchLocationQuantityCommand);
                });
                ReduceProductBatchQuantityCommand reduceProductBatchQuantityCommand = ReduceProductBatchQuantityCommand.builder()
                        .batchId(evtExportInvoiceDetailBatch.getBatchId())
                        .quantity(evtExportInvoiceDetailBatch.getQuantity())
                        .build();
                commandGateway.sendAndWait(reduceProductBatchQuantityCommand);
            });

            GetProductStockQuantityQuery getProductStockQuantityQuery = GetProductStockQuantityQuery.builder()
                    .productId(evtExportInvoiceDetail.getProductId())
                    .productUnitId(evtExportInvoiceDetail.getProductUnitId())
                    .quantity(Double.valueOf(evtExportInvoiceDetail.getQuantity()))
                    .build();
            Double productStockQuantity = queryGateway.query(getProductStockQuantityQuery, ResponseTypes.instanceOf(Double.class)).join();
            ReduceProductStockCommand reduceProductStockCommand = ReduceProductStockCommand.builder()
                    .warehouseId(exportInvoiceCreatedEvent.getWarehouseId())
                    .productId(evtExportInvoiceDetail.getProductId())
                    .quantity(productStockQuantity)
                    .build();
            commandGateway.sendAndWait(reduceProductStockCommand);

            ReduceProductTotalQuantityCommand reduceProductTotalQuantityCommand = ReduceProductTotalQuantityCommand.builder()
                    .productId(evtExportInvoiceDetail.getProductId())
                    .quantity(productStockQuantity)
                    .build();
            commandGateway.sendAndWait(reduceProductTotalQuantityCommand);
        });

        CreateDebtAccountCommand createDebtAccountCommand = CreateDebtAccountCommand.builder()
                .debtAccountId(UUID.randomUUID().toString())
                .partyId(exportInvoiceCreatedEvent.getCustomerId())
                .partyType(DebtPartyType.CUSTOMER)
                .sourceId(exportInvoiceCreatedEvent.getExportInvoiceId())
                .sourceType(DebtSourceType.EXPORT_INVOICE)
                .totalAmount(exportInvoiceCreatedEvent.getFinalAmount())
                .paidAmount(BigDecimal.ZERO)
                .remainingAmount(exportInvoiceCreatedEvent.getFinalAmount())
                .interestRate(SystemConfigConstants.OVERDUE_INTEREST_RATE)
                .dueDate(exportInvoiceCreatedEvent.getCreatedDate().plusDays(SystemConfigConstants.OVERDUE_DEBT_DAYS))
                .debtStatus(DebtStatus.UNPAID)
                .build();
        commandGateway.sendAndWait(createDebtAccountCommand);
    }
}
