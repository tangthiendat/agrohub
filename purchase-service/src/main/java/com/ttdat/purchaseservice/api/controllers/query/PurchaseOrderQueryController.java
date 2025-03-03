package com.ttdat.purchaseservice.api.controllers.query;

import com.ttdat.core.api.dto.request.PaginationParams;
import com.ttdat.core.api.dto.request.SortParams;
import com.ttdat.core.api.dto.response.ApiResponse;
import com.ttdat.core.infrastructure.utils.RequestParamsUtils;
import com.ttdat.purchaseservice.api.dto.common.PurchaseOrderDTO;
import com.ttdat.purchaseservice.api.dto.response.PurchaseOrderPageResult;
import com.ttdat.purchaseservice.application.queries.purchaseorder.GetPurchaseOrderByIdQuery;
import com.ttdat.purchaseservice.application.queries.purchaseorder.GetPurchaseOrderPageQuery;
import lombok.RequiredArgsConstructor;
import org.axonframework.messaging.responsetypes.ResponseTypes;
import org.axonframework.queryhandling.QueryGateway;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/v1/purchase-orders")
@RequiredArgsConstructor
public class PurchaseOrderQueryController {
    private final QueryGateway queryGateway;

    @GetMapping("/page")
    public ResponseEntity<ApiResponse<PurchaseOrderPageResult>> getPurchaseOrderPage(@RequestParam Map<String, String> filterParams) {
        PaginationParams paginationParams = RequestParamsUtils.getPaginationParams(filterParams);
        SortParams sortParams = RequestParamsUtils.getSortParams(filterParams);
        GetPurchaseOrderPageQuery getPurchaseOrderPageQuery = GetPurchaseOrderPageQuery.builder()
                .paginationParams(paginationParams)
                .sortParams(sortParams)
                .filterParams(filterParams)
                .build();
        PurchaseOrderPageResult purchaseOrderPageResult = queryGateway.query(getPurchaseOrderPageQuery, ResponseTypes.instanceOf(PurchaseOrderPageResult.class)).join();
        return ResponseEntity.status(HttpStatus.OK)
                .body(ApiResponse.<PurchaseOrderPageResult>builder()
                        .status(HttpStatus.OK.value())
                        .message("Get purchase order page successfully")
                        .success(true)
                        .payload(purchaseOrderPageResult)
                        .build()
                );
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<PurchaseOrderDTO>> getPurchaseOrderById(@PathVariable("id") String id) {
        GetPurchaseOrderByIdQuery getPurchaseOrderByIdQuery = GetPurchaseOrderByIdQuery.builder()
                .purchaseOrderId(id)
                .build();
        PurchaseOrderDTO purchaseOrderDTO = queryGateway.query(getPurchaseOrderByIdQuery, ResponseTypes.instanceOf(PurchaseOrderDTO.class)).join();
        return ResponseEntity.status(HttpStatus.OK)
                .body(ApiResponse.<PurchaseOrderDTO>builder()
                        .status(HttpStatus.OK.value())
                        .message("Get purchase order successfully")
                        .success(true)
                        .payload(purchaseOrderDTO)
                        .build()
                );
    }
}
