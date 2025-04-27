package com.ttdat.purchaseservice.api.controllers.query;

import com.ttdat.core.api.dto.response.ApiResponse;
import com.ttdat.purchaseservice.api.dto.common.SupplierDTO;
import com.ttdat.purchaseservice.application.queries.supplier.GetSupplierByProductIdQuery;
import lombok.RequiredArgsConstructor;
import org.axonframework.messaging.responsetypes.ResponseTypes;
import org.axonframework.queryhandling.QueryGateway;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/v1/supplier-products")
@RequiredArgsConstructor
public class SupplierProductQueryController {
    private final QueryGateway queryGateway;

    @GetMapping("/product/{productId}")
    public ApiResponse<List<SupplierDTO>> getSupplier(@PathVariable String productId) {
        GetSupplierByProductIdQuery getSupplierByProductIdQuery = GetSupplierByProductIdQuery.builder()
                .productId(productId)
                .build();
        List<SupplierDTO> suppliers = queryGateway.query(getSupplierByProductIdQuery, ResponseTypes.multipleInstancesOf(SupplierDTO.class)).join();
        return ApiResponse.<List<SupplierDTO>>builder()
                .status(HttpStatus.OK.value())
                .message("Supplier fetched successfully")
                .payload(suppliers)
                .success(true)
                .build();
    }
}
