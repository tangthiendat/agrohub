package com.ttdat.salesservice.api.controllers.query;

import com.ttdat.core.api.dto.response.ApiResponse;
import com.ttdat.core.api.dto.response.StatsCardValue;
import com.ttdat.core.application.queries.stats.GetExportInvoiceCountQuery;
import com.ttdat.core.application.queries.stats.GetImportInvoiceCountQuery;
import com.ttdat.core.infrastructure.utils.NumberUtils;
import lombok.RequiredArgsConstructor;
import org.axonframework.messaging.responsetypes.ResponseTypes;
import org.axonframework.queryhandling.QueryGateway;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;
import java.time.LocalDate;

@RestController
@RequestMapping("/api/v1/invoices")
@RequiredArgsConstructor
public class InvoiceQueryController {
    private final QueryGateway queryGateway;

    @GetMapping("/stats/card")
    public ApiResponse<StatsCardValue> getOrderStatsCard(){
        LocalDate today = LocalDate.now();
        LocalDate startOfMonth = today.withDayOfMonth(1);
        GetExportInvoiceCountQuery getExportInvoiceCountQuery = GetExportInvoiceCountQuery.builder()
                .startDate(startOfMonth)
                .endDate(today)
                .build();
        Long currentExpInvoiceCount = queryGateway.query(getExportInvoiceCountQuery, ResponseTypes.instanceOf(Long.class)).join();
        GetImportInvoiceCountQuery getImportInvoiceCountQuery = GetImportInvoiceCountQuery.builder()
                .startDate(startOfMonth)
                .endDate(today)
                .build();
        Long currentImpInvoiceCount = queryGateway.query(getImportInvoiceCountQuery, ResponseTypes.instanceOf(Long.class)).join();
        BigDecimal currentTotalInvoiceCount = BigDecimal.valueOf(currentExpInvoiceCount + currentImpInvoiceCount);
        LocalDate startOfPreviousMonth = today.minusMonths(1).withDayOfMonth(1);
        LocalDate endOfPreviousMonth = startOfPreviousMonth.plusMonths(1).minusDays(1);
        GetExportInvoiceCountQuery getPreviousMonthExportInvoiceCountQuery = GetExportInvoiceCountQuery.builder()
                .startDate(startOfPreviousMonth)
                .endDate(endOfPreviousMonth)
                .build();
        Long previousMonthExportInvoiceCount = queryGateway.query(getPreviousMonthExportInvoiceCountQuery, ResponseTypes.instanceOf(Long.class)).join();
        GetImportInvoiceCountQuery getPreviousMonthImportInvoiceCountQuery = GetImportInvoiceCountQuery.builder()
                .startDate(startOfPreviousMonth)
                .endDate(endOfPreviousMonth)
                .build();
        Long previousMonthImportInvoiceCount = queryGateway.query(getPreviousMonthImportInvoiceCountQuery, ResponseTypes.instanceOf(Long.class)).join();
        BigDecimal previousTotalInvoiceCount = BigDecimal.valueOf(previousMonthExportInvoiceCount + previousMonthImportInvoiceCount);
        StatsCardValue statsCardValue = StatsCardValue.builder()
                .value(currentTotalInvoiceCount)
                .changePercentage(NumberUtils.getChangePercentage(previousTotalInvoiceCount, currentTotalInvoiceCount))
                .trend(NumberUtils.getTrendType(previousTotalInvoiceCount, currentTotalInvoiceCount))
                .build();
        return ApiResponse.<StatsCardValue>builder()
                .status(HttpStatus.OK.value())
                .message("Get invoice stats card successfully")
                .success(true)
                .payload(statsCardValue)
                .build();
    }
}
