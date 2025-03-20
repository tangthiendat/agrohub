package com.ttdat.purchaseservice.api.controllers.query;

import com.ttdat.core.api.dto.request.PaginationParams;
import com.ttdat.core.api.dto.request.SortParams;
import com.ttdat.core.api.dto.response.ApiResponse;
import com.ttdat.core.infrastructure.utils.RequestParamsUtils;
import com.ttdat.purchaseservice.api.dto.response.ImportInvoicePageResult;
import com.ttdat.purchaseservice.application.queries.importinvoice.GetImportInvoicePageQuery;
import lombok.RequiredArgsConstructor;
import org.axonframework.messaging.responsetypes.ResponseTypes;
import org.axonframework.queryhandling.QueryGateway;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

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

}
