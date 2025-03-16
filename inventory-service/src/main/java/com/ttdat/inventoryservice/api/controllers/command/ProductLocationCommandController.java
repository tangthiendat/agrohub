package com.ttdat.inventoryservice.api.controllers.command;

import com.ttdat.core.api.dto.response.ApiResponse;
import com.ttdat.inventoryservice.api.dto.common.ProductLocationDTO;
import com.ttdat.inventoryservice.application.services.ProductLocationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/product-locations")
@RequiredArgsConstructor
public class ProductLocationCommandController {
    private final ProductLocationService productLocationService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ApiResponse<Object> createProductLocation(@RequestBody ProductLocationDTO productLocationDTO) {
        productLocationService.createProductLocation(productLocationDTO);
        return ApiResponse.builder()
                .status(HttpStatus.CREATED.value())
                .message("Product location created successfully")
                .success(true)
                .build();
    }

    @PutMapping("/{id}")
    public ApiResponse<Object> updateProductLocation(@PathVariable String id, @RequestBody ProductLocationDTO productLocationDTO    ) {
        productLocationService.updateProductLocation(id, productLocationDTO);
        return ApiResponse.builder()
                .status(HttpStatus.OK.value())
                .message("Product location updated successfully")
                .success(true)
                .build();
    }
}
