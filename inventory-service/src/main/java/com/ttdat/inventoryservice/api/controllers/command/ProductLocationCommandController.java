package com.ttdat.inventoryservice.api.controllers.command;

import com.ttdat.core.api.dto.response.ApiResponse;
import com.ttdat.inventoryservice.api.dto.request.CreateProductLocationRequest;
import com.ttdat.inventoryservice.api.dto.request.UpdateProductLocationRequest;
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
    public ApiResponse<Object> createProductLocation(@RequestBody CreateProductLocationRequest createProductLocationRequest) {
        productLocationService.createProductLocation(createProductLocationRequest);
        return ApiResponse.builder()
                .status(HttpStatus.CREATED.value())
                .message("Product location created successfully")
                .success(true)
                .build();
    }

    @PutMapping("/{id}")
    public ApiResponse<Object> updateProductLocation(@PathVariable String id, @RequestBody UpdateProductLocationRequest updateProductLocationRequest) {
        productLocationService.updateProductLocation(id, updateProductLocationRequest);
        return ApiResponse.builder()
                .status(HttpStatus.OK.value())
                .message("Product location updated successfully")
                .success(true)
                .build();
    }
}
