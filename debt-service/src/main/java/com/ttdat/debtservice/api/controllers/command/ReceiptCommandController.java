package com.ttdat.debtservice.api.controllers.command;

import com.ttdat.core.api.dto.response.ApiResponse;
import com.ttdat.debtservice.api.dto.request.CreateReceiptRequest;
import com.ttdat.debtservice.application.services.ReceiptService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/receipts")
@RequiredArgsConstructor
public class ReceiptCommandController {
    private final ReceiptService receiptService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ApiResponse<Object> createReceipt(@RequestBody @Valid CreateReceiptRequest createReceiptRequest) {
        receiptService.createReceipt(createReceiptRequest);
        return ApiResponse.builder()
                .message("Receipt created successfully")
                .success(true)
                .status(HttpStatus.CREATED.value())
                .build();
    }

}
