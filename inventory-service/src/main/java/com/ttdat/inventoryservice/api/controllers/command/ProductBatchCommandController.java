package com.ttdat.inventoryservice.api.controllers.command;

import com.ttdat.core.api.dto.response.ApiResponse;
import com.ttdat.inventoryservice.api.dto.common.ProductBatchDTO;
import com.ttdat.inventoryservice.application.services.ProductBatchService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/product-batches")
@RequiredArgsConstructor
public class ProductBatchCommandController {
    private final ProductBatchService productBatchService;

    @PatchMapping("/{id}")
    public ApiResponse<Object> updateBatch(@PathVariable String id, @RequestBody ProductBatchDTO productBatchDTO) {
        productBatchService.updateProductBatchLocation(id, productBatchDTO);
        return ApiResponse.builder()
                .status(HttpStatus.OK.value())
                .message("Product batch updated successfully")
                .success(true)
                .build();
    }
}
