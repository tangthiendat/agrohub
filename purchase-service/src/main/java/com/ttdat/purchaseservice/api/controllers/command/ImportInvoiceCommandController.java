package com.ttdat.purchaseservice.api.controllers.command;

import com.ttdat.core.api.dto.response.ApiResponse;
import com.ttdat.purchaseservice.api.dto.request.CreateImportInvoiceRequest;
import com.ttdat.purchaseservice.application.services.ImportInvoiceService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/import-invoices")
@RequiredArgsConstructor
public class ImportInvoiceCommandController {
    private final ImportInvoiceService importInvoiceService;

    @PostMapping
    public ResponseEntity<ApiResponse<Object>> createImportInvoice(@RequestBody CreateImportInvoiceRequest createImportInvoiceRequest) {
        importInvoiceService.createImportInvoice(createImportInvoiceRequest);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.builder()
                        .status(HttpStatus.CREATED.value())
                        .message("Import invoice created successfully")
                        .success(true)
                        .build());
    }
}
