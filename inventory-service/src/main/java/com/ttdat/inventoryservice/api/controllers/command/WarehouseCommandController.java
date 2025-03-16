package com.ttdat.inventoryservice.api.controllers.command;

import com.ttdat.core.api.dto.response.ApiResponse;
import com.ttdat.inventoryservice.api.dto.common.WarehouseDTO;
import com.ttdat.inventoryservice.application.services.WarehouseService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/warehouses")
@RequiredArgsConstructor
public class WarehouseCommandController {
    private final WarehouseService warehouseService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ApiResponse<Object> createWarehouse(@Valid @RequestBody WarehouseDTO warehouseDTO) {
        warehouseService.createWarehouse(warehouseDTO);
        return ApiResponse.builder()
                .status(HttpStatus.CREATED.value())
                .success(true)
                .message("Warehouse created successfully")
                .build();
    }

    @PutMapping({"/{id}"})
    public ApiResponse<Object> updateWarehouse(@PathVariable Long id, @Valid @RequestBody WarehouseDTO warehouseDTO) {
        warehouseService.updateWarehouse(id, warehouseDTO);
        return ApiResponse.builder()
                .status(HttpStatus.OK.value())
                .success(true)
                .message("Warehouse updated successfully")
                .build();
    }

}
