package com.ttdat.purchaseservice.api.controllers.command;

import com.ttdat.core.api.dto.response.ApiResponse;
import com.ttdat.purchaseservice.api.dto.request.CancelPurchaseOrderRequest;
import com.ttdat.purchaseservice.api.dto.request.CreatePurchaseOrderRequest;
import com.ttdat.purchaseservice.api.dto.request.UpdatePurchaseOrderRequest;
import com.ttdat.purchaseservice.api.dto.request.UpdatePurchaseOrderStatusRequest;
import com.ttdat.purchaseservice.application.services.PurchaseOrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/purchase-orders")
@RequiredArgsConstructor
public class PurchaseOrderCommandController {
    private final PurchaseOrderService purchaseOrderService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ApiResponse<Object> createPurchaseOrder(@RequestBody CreatePurchaseOrderRequest createPurchaseOrderRequest) {
        purchaseOrderService.createPurchaseOrder(createPurchaseOrderRequest);
        return ApiResponse.builder()
                .status(HttpStatus.CREATED.value())
                .message("Purchase order created successfully")
                .success(true)
                .build();
    }

    @PatchMapping("/{id}/status")
    public ApiResponse<Object> updatePurchaseOrderStatus(@PathVariable String id, @RequestBody UpdatePurchaseOrderStatusRequest updatePurchaseOrderStatusRequest) {
        purchaseOrderService.updatePurchaseOrderStatus(id, updatePurchaseOrderStatusRequest);
        return ApiResponse.builder()
                .status(HttpStatus.OK.value())
                .message("Purchase order status updated successfully")
                .success(true)
                .build();
    }

    @PutMapping("/{id}")
    public ApiResponse<Object> updatePurchaseOrder(@PathVariable String id, @RequestBody UpdatePurchaseOrderRequest updatePurchaseOrderRequest) {
        purchaseOrderService.updatePurchaseOrder(id, updatePurchaseOrderRequest);
        return ApiResponse.builder()
                .status(HttpStatus.OK.value())
                .message("Purchase order updated successfully")
                .success(true)
                .build();
    }

    @PatchMapping("/{id}/cancel")
    public ApiResponse<Object> cancelPurchaseOrder(@PathVariable String id, @RequestBody CancelPurchaseOrderRequest cancelPurchaseOrderRequest) {
        purchaseOrderService.cancelPurchaseOrder(id, cancelPurchaseOrderRequest);
        return ApiResponse.builder()
                .status(HttpStatus.OK.value())
                .message("Purchase order canceled successfully")
                .success(true)
                .build();
    }

}
