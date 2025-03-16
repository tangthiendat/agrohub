package com.ttdat.purchaseservice.api.controllers.command;

import com.ttdat.core.api.dto.response.ApiResponse;
import com.ttdat.purchaseservice.api.dto.common.SupplierDTO;
import com.ttdat.purchaseservice.api.dto.request.CreateSupplierRatingRequest;
import com.ttdat.purchaseservice.api.dto.request.UpdateSupplierRatingRequest;
import com.ttdat.purchaseservice.api.dto.request.UpdateSupplierStatusRequest;
import com.ttdat.purchaseservice.application.services.SupplierService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/suppliers")
@RequiredArgsConstructor
public class SupplierCommandController {
    private final SupplierService supplierService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ApiResponse<Object> createSupplier(@Valid @RequestBody SupplierDTO supplierDTO) {
        supplierService.createSupplier(supplierDTO);
        return ApiResponse.builder()
                .status(HttpStatus.CREATED.value())
                .message("Supplier created successfully")
                .success(true)
                .build();
    }

    @PutMapping("/{id}")
    public ApiResponse<Object> updateSupplier(@PathVariable String id, @Valid @RequestBody SupplierDTO supplierDTO) {
        supplierService.updateSupplier(id, supplierDTO);
        return ApiResponse.builder()
                .status(HttpStatus.OK.value())
                .message("Supplier updated successfully")
                .success(true)
                .build();
    }

    @PatchMapping("/{id}/status")
    public ApiResponse<Object> updateSupplierStatus(@PathVariable String id, @Valid @RequestBody UpdateSupplierStatusRequest updateSupplierStatusRequest) {
        supplierService.updateSupplierStatus(id, updateSupplierStatusRequest);
        return ApiResponse.builder()
                .status(HttpStatus.OK.value())
                .message("Supplier status updated successfully")
                .success(true)
                .build();
    }

    @PostMapping("/{id}/ratings")
    @ResponseStatus(HttpStatus.CREATED)
    public ApiResponse<Object> createSupplierRating(@PathVariable String id, @RequestBody CreateSupplierRatingRequest createSupplierRatingRequest) {
        supplierService.createSupplierRating(id, createSupplierRatingRequest);
        return ApiResponse.builder()
                .status(HttpStatus.CREATED.value())
                .message("Supplier rating created successfully")
                .success(true)
                .build();
    }

    @PatchMapping("/{id}/ratings/{ratingId}")
    public ApiResponse<Object> updateSupplierRating(@PathVariable String id, @PathVariable String ratingId,
                                                    @Valid @RequestBody UpdateSupplierRatingRequest updateSupplierRatingRequest) {
        supplierService.updateSupplierRating(id, ratingId, updateSupplierRatingRequest);
        return ApiResponse.builder()
                .status(HttpStatus.OK.value())
                .message("Supplier rating updated successfully")
                .success(true)
                .build();
    }

}
