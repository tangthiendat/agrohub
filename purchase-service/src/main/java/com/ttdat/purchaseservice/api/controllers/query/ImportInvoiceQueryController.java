package com.ttdat.purchaseservice.api.controllers.query;

import com.ttdat.core.api.dto.request.PaginationParams;
import com.ttdat.core.api.dto.request.SortParams;
import com.ttdat.core.api.dto.response.ApiResponse;
import com.ttdat.core.api.dto.response.StatsCardValue;
import com.ttdat.core.infrastructure.utils.NumberUtils;
import com.ttdat.core.infrastructure.utils.RequestParamsUtils;
import com.ttdat.purchaseservice.api.dto.response.ImportInvoicePageResult;
import com.ttdat.purchaseservice.application.queries.importinvoice.GetImportInvoicePageQuery;
import com.ttdat.purchaseservice.application.queries.importinvoice.GetTotalImportInRangeQuery;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.axonframework.messaging.responsetypes.ResponseTypes;
import org.axonframework.queryhandling.QueryGateway;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/api/v1/import-invoices")
@RequiredArgsConstructor
public class ImportInvoiceQueryController {
    private final QueryGateway queryGateway;

    @GetMapping("/page")
    public ApiResponse<ImportInvoicePageResult> getImportInvoicePage(@RequestParam Map<String, String> filterParams) {
        PaginationParams paginationParams = RequestParamsUtils.getPaginationParams(filterParams);
        SortParams sortParams = RequestParamsUtils.getSortParams(filterParams);
        GetImportInvoicePageQuery getImportInvoicePageQuery = GetImportInvoicePageQuery.builder()
                .paginationParams(paginationParams)
                .sortParams(sortParams)
                .filterParams(filterParams)
                .build();
        ImportInvoicePageResult importInvoicePageResult = queryGateway.query(getImportInvoicePageQuery, ResponseTypes.instanceOf(ImportInvoicePageResult.class)).join();
        return ApiResponse.<ImportInvoicePageResult>builder()
                .status(HttpStatus.OK.value())
                .message("Get import invoice page successfully")
                .success(true)
                .payload(importInvoicePageResult)
                .build();
    }

    @GetMapping("/stats/card")
    public ApiResponse<StatsCardValue> getImportStatsCard(){
        LocalDate today = LocalDate.now();
        LocalDate startOfMonth = today.withDayOfMonth(1);
        GetTotalImportInRangeQuery getTotalImportInRangeQuery = GetTotalImportInRangeQuery.builder()
                .startDate(startOfMonth)
                .endDate(today)
                .build();
        BigDecimal currentTotalImport = queryGateway.query(getTotalImportInRangeQuery, ResponseTypes.instanceOf(BigDecimal.class)).join();
        LocalDate startOfPreviousMonth = today.minusMonths(1).withDayOfMonth(1);
        LocalDate endOfPreviousMonth = startOfPreviousMonth.plusMonths(1).minusDays(1);
        GetTotalImportInRangeQuery getPreviousMonthTotalImportQuery = GetTotalImportInRangeQuery.builder()
                .startDate(startOfPreviousMonth)
                .endDate(endOfPreviousMonth)
                .build();
        BigDecimal previousTotalImport = queryGateway.query(getPreviousMonthTotalImportQuery, ResponseTypes.instanceOf(BigDecimal.class)).join();
        log.info("Current total import: {}, Previous total import: {}", currentTotalImport, previousTotalImport);
        StatsCardValue statsCardValue = StatsCardValue.builder()
                .value(currentTotalImport)
                .changePercentage(NumberUtils.getChangePercentage(previousTotalImport, currentTotalImport))
                .trend(NumberUtils.getTrendType(previousTotalImport, currentTotalImport))
                .build();
        return ApiResponse.<StatsCardValue>builder()
                .status(HttpStatus.OK.value())
                .message("Get import invoice stats card successfully")
                .success(true)
                .payload(statsCardValue)
                .build();
    }

}
