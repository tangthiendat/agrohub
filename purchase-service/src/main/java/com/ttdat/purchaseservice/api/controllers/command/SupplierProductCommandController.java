package com.ttdat.purchaseservice.api.controllers.command;

import com.ttdat.core.api.dto.response.ApiResponse;
import com.ttdat.purchaseservice.api.dto.common.SupplierProductDTO;
import com.ttdat.purchaseservice.application.services.SupplierService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/supplier-products")
@RequiredArgsConstructor
public class SupplierProductCommandController {
    private final SupplierService supplierService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ApiResponse<Object> createSupplierProduct(@Valid @RequestBody SupplierProductDTO supplierProductDTO) {
        supplierService.createSupplierProduct(supplierProductDTO);
        return ApiResponse.builder()
                .status(HttpStatus.CREATED.value())
                .success(true)
                .message("Supplier product created successfully")
                .build();
    }
}
