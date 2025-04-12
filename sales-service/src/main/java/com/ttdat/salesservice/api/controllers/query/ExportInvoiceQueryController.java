package com.ttdat.salesservice.api.controllers.query;

import com.ttdat.core.api.dto.request.PaginationParams;
import com.ttdat.core.api.dto.request.SortParams;
import com.ttdat.core.api.dto.response.ApiResponse;
import com.ttdat.core.infrastructure.utils.RequestParamsUtils;
import com.ttdat.salesservice.api.dto.response.ExportInvoicePageResult;
import com.ttdat.salesservice.application.queries.exportinvoice.GetExportInvoicePageQuery;
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
}
