package com.ttdat.inventoryservice.api.controllers.query;

import com.ttdat.core.api.dto.request.PaginationParams;
import com.ttdat.core.api.dto.request.SortParams;
import com.ttdat.core.api.dto.response.ApiResponse;
import com.ttdat.core.infrastructure.utils.RequestParamsUtils;
import com.ttdat.inventoryservice.api.dto.common.WarehouseDTO;
import com.ttdat.inventoryservice.api.dto.response.WarehousePageResult;
import com.ttdat.inventoryservice.application.queries.warehouse.GetAllWarehouseQuery;
import com.ttdat.inventoryservice.application.queries.warehouse.GetCurrentUserWarehouseQuery;
import com.ttdat.inventoryservice.application.queries.warehouse.GetWarehouseByIdQuery;
import com.ttdat.inventoryservice.application.queries.warehouse.GetWarehousePageQuery;
import lombok.RequiredArgsConstructor;
import org.axonframework.messaging.responsetypes.ResponseTypes;
import org.axonframework.queryhandling.QueryGateway;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
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

    @GetMapping
    public ResponseEntity<ApiResponse<List<WarehouseDTO>>> getWarehouse() {
        GetAllWarehouseQuery getAllWarehouseQuery = GetAllWarehouseQuery.builder().build();
        List<WarehouseDTO> warehouseDTOs = queryGateway.query(getAllWarehouseQuery, ResponseTypes.multipleInstancesOf(WarehouseDTO.class)).join();
        return ResponseEntity.ok(
                ApiResponse.<List<WarehouseDTO>>builder()
                        .status(HttpStatus.OK.value())
                        .success(true)
                        .message("Get warehouse successfully")
                        .payload(warehouseDTOs)
                        .build()
        );
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<WarehouseDTO>> getWarehouseById(@PathVariable Long id) {
        GetWarehouseByIdQuery getWarehouseById = GetWarehouseByIdQuery.builder().warehouseId(id).build();
        WarehouseDTO warehouseDTO = queryGateway.query(getWarehouseById, ResponseTypes.instanceOf(WarehouseDTO.class)).join();
        return ResponseEntity.ok(
                ApiResponse.<WarehouseDTO>builder()
                        .status(HttpStatus.OK.value())
                        .success(true)
                        .message("Get warehouse by id successfully")
                        .payload(warehouseDTO)
                        .build()
        );
    }

    @GetMapping("/me")
    public ResponseEntity<ApiResponse<WarehouseDTO>> getMyWarehouse() {
        GetCurrentUserWarehouseQuery getCurrentUserWarehouseQuery = GetCurrentUserWarehouseQuery.builder().build();
        WarehouseDTO warehouseDTO = queryGateway.query(getCurrentUserWarehouseQuery, ResponseTypes.instanceOf(WarehouseDTO.class)).join();
        return ResponseEntity.ok(
                ApiResponse.<WarehouseDTO>builder()
                        .status(HttpStatus.OK.value())
                        .success(true)
                        .message("Get my warehouse successfully")
                        .payload(warehouseDTO)
                        .build()
        );
    }
}
