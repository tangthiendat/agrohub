package com.ttdat.purchaseservice.api.controllers.command;

import com.ttdat.core.api.dto.response.ApiResponse;
import com.ttdat.purchaseservice.api.dto.common.SupplierDTO;
import com.ttdat.purchaseservice.api.dto.request.UpdateSupplierStatusRequest;
import com.ttdat.purchaseservice.application.services.SupplierService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/suppliers")
@RequiredArgsConstructor
public class SupplierCommandController {
    private final SupplierService supplierService;

    @PostMapping
    public ResponseEntity<ApiResponse<Object>> createSupplier(@Valid @RequestBody SupplierDTO supplierDTO) {
        supplierService.createSupplier(supplierDTO);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.builder()
                        .status(HttpStatus.CREATED.value())
                        .message("Supplier created successfully")
                        .success(true)
                        .build()
                );
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<Object>> updateSupplier(@PathVariable String id, @Valid @RequestBody SupplierDTO supplierDTO) {
        supplierService.updateSupplier(id, supplierDTO);
        return ResponseEntity.ok(
                ApiResponse.builder()
                        .status(HttpStatus.OK.value())
                        .message("Supplier updated successfully")
                        .success(true)
                        .build()
        );
    }

    @PatchMapping("/{id}/status")
    public ResponseEntity<ApiResponse<Object>> updateSupplierStatus(@PathVariable String id, @Valid @RequestBody UpdateSupplierStatusRequest updateSupplierStatusRequest) {
        supplierService.updateSupplierStatus(id, updateSupplierStatusRequest);
        return ResponseEntity.ok(
                ApiResponse.builder()
                        .status(HttpStatus.OK.value())
                        .message("Supplier status updated successfully")
                        .success(true)
                        .build()
        );
    }

}
