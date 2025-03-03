package com.ttdat.purchaseservice.api.controllers.command;

import com.ttdat.core.api.dto.response.ApiResponse;
import com.ttdat.purchaseservice.api.dto.request.CreatePurchaseOrderRequest;
import com.ttdat.purchaseservice.application.services.PurchaseOrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/purchase-orders")
@RequiredArgsConstructor
public class PurchaseOrderCommandController {
    private final PurchaseOrderService purchaseOrderService;

    @PostMapping
    public ResponseEntity<ApiResponse<Object>> createPurchaseOrder(@RequestBody CreatePurchaseOrderRequest createPurchaseOrderRequest) {
        purchaseOrderService.createPurchaseOrder(createPurchaseOrderRequest);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(
                        ApiResponse.builder()
                                .status(HttpStatus.CREATED.value())
                                .message("Purchase order created successfully")
                                .success(true)
                                .build()
                );
    }

}
