package com.ttdat.salesservice.api.controllers.query;

import com.ttdat.core.api.dto.request.PaginationParams;
import com.ttdat.core.api.dto.request.SortParams;
import com.ttdat.core.api.dto.response.ApiResponse;
import com.ttdat.core.api.dto.response.StatsCardValue;
import com.ttdat.core.infrastructure.utils.NumberUtils;
import com.ttdat.core.infrastructure.utils.RequestParamsUtils;
import com.ttdat.salesservice.api.dto.response.ExportInvoicePageResult;
import com.ttdat.salesservice.application.queries.exportinvoice.GetExportInvoicePageQuery;
import com.ttdat.salesservice.application.queries.exportinvoice.GetTotalExportInRangeQuery;
import lombok.RequiredArgsConstructor;
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

@RestController
@RequestMapping("/api/v1/export-invoices")
@RequiredArgsConstructor
public class ExportInvoiceQueryController {
    private final QueryGateway queryGateway;

    @GetMapping("/page")
    public ApiResponse<ExportInvoicePageResult> getImportInvoicePage(@RequestParam Map<String, String> filterParams) {
        PaginationParams paginationParams = RequestParamsUtils.getPaginationParams(filterParams);
        SortParams sortParams = RequestParamsUtils.getSortParams(filterParams);
        GetExportInvoicePageQuery getImportInvoicePageQuery = GetExportInvoicePageQuery.builder()
                .paginationParams(paginationParams)
                .sortParams(sortParams)
                .filterParams(filterParams)
                .build();
        ExportInvoicePageResult exportInvoicePageResult = queryGateway.query(getImportInvoicePageQuery, ResponseTypes.instanceOf(ExportInvoicePageResult.class)).join();
        return ApiResponse.<ExportInvoicePageResult>builder()
                .status(HttpStatus.OK.value())
                .message("Get export invoice page successfully")
                .success(true)
                .payload(exportInvoicePageResult)
                .build();
    }

    @GetMapping("/stats/card")
    public ApiResponse<StatsCardValue> getExportStatsCard() {
        LocalDate today = LocalDate.now();
        LocalDate startOfMonth = today.withDayOfMonth(1);
        GetTotalExportInRangeQuery getTotalExportInRangeQuery = GetTotalExportInRangeQuery.builder()
                .startDate(startOfMonth)
                .endDate(today)
                .build();
        BigDecimal currentTotalExport = queryGateway.query(getTotalExportInRangeQuery, ResponseTypes.instanceOf(BigDecimal.class)).join();

        LocalDate startOfPreviousMonth = today.minusMonths(1).withDayOfMonth(1);
        LocalDate endOfPreviousMonth = startOfPreviousMonth.plusMonths(1).minusDays(1);
        GetTotalExportInRangeQuery getPreviousMonthTotalExportQuery = GetTotalExportInRangeQuery.builder()
                .startDate(startOfPreviousMonth)
                .endDate(endOfPreviousMonth)
                .build();
        BigDecimal previousTotalExport = queryGateway.query(getPreviousMonthTotalExportQuery, ResponseTypes.instanceOf(BigDecimal.class)).join();

        StatsCardValue statsCardValue = StatsCardValue.builder()
                .value(currentTotalExport)
                .changePercentage(NumberUtils.getChangePercentage(previousTotalExport, currentTotalExport))
                .trend(NumberUtils.getTrendType(previousTotalExport, currentTotalExport))
                .build();

        return ApiResponse.<StatsCardValue>builder()
                .status(HttpStatus.OK.value())
                .message("Get export invoice stats card successfully")
                .success(true)
                .payload(statsCardValue)
                .build();
    }

}
