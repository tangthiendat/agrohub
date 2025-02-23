package com.ttdat.inventoryservice.api.controllers.query;

import com.ttdat.core.api.dto.request.PaginationParams;
import com.ttdat.core.api.dto.request.SortParams;
import com.ttdat.core.api.dto.response.ApiResponse;
import com.ttdat.core.infrastructure.utils.RequestParamsUtils;
import com.ttdat.inventoryservice.api.dto.response.WarehousePageResult;
import com.ttdat.inventoryservice.application.queries.GetWarehousePageQuery;
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
@RequestMapping("/api/v1/warehouses")
@RequiredArgsConstructor
public class WarehouseQueryController {
    private final QueryGateway queryGateway;

    @GetMapping("/page")
    public ResponseEntity<ApiResponse<WarehousePageResult>> getWarehousePage(@RequestParam Map<String, String> filterParams) {
        PaginationParams paginationParams = RequestParamsUtils.getPaginationParams(filterParams);
        SortParams sortParams = RequestParamsUtils.getSortParams(filterParams);
        GetWarehousePageQuery getWarehousePageQuery = GetWarehousePageQuery.builder()
                .paginationParams(paginationParams)
                .sortParams(sortParams)
                .build();
        WarehousePageResult warehousePageResult = queryGateway.query(getWarehousePageQuery, ResponseTypes.instanceOf(WarehousePageResult.class)).join();
        return ResponseEntity.ok(
                ApiResponse.<WarehousePageResult>builder()
                        .status(HttpStatus.OK.value())
                        .success(true)
                        .message("Get warehouse page successfully")
                        .payload(warehousePageResult)
                        .build()
        );
    }
}
