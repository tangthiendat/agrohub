package com.ttdat.purchaseservice.api.controllers.query;

import com.ttdat.core.api.dto.request.PaginationParams;
import com.ttdat.core.api.dto.request.SortParams;
import com.ttdat.core.api.dto.response.ApiResponse;
import com.ttdat.core.infrastructure.utils.RequestParamsUtils;
import com.ttdat.purchaseservice.api.dto.response.SupplierPageResult;
import com.ttdat.purchaseservice.application.queries.supplier.GetSupplierPageQuery;
import lombok.RequiredArgsConstructor;
import org.axonframework.messaging.responsetypes.ResponseTypes;
import org.axonframework.queryhandling.QueryGateway;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/api/v1/suppliers")
@RequiredArgsConstructor
public class SupplierQueryController {
    private final QueryGateway queryGateway;

    @GetMapping("/page")
    public ResponseEntity<ApiResponse<SupplierPageResult>> getSupplierPage(@RequestParam Map<String, String> filterParams) {
        PaginationParams paginationParams = RequestParamsUtils.getPaginationParams(filterParams);
        SortParams sortParams = RequestParamsUtils.getSortParams(filterParams);
        GetSupplierPageQuery getSupplierPageQuery = GetSupplierPageQuery.builder()
                .paginationParams(paginationParams)
                .sortParams(sortParams)
                .filterParams(filterParams)
                .build();
        SupplierPageResult supplierPageResult = queryGateway.query(getSupplierPageQuery, ResponseTypes.instanceOf(SupplierPageResult.class)).join();
        return ResponseEntity.ok(
                ApiResponse.<SupplierPageResult>builder()
                        .status(HttpStatus.OK.value())
                        .message("Get supplier page successfully")
                        .success(true)
                        .payload(supplierPageResult)
                        .build()
        );
    }
}
