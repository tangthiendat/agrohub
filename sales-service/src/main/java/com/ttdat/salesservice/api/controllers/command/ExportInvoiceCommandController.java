package com.ttdat.salesservice.api.controllers.command;

import com.ttdat.core.api.dto.response.ApiResponse;
import com.ttdat.salesservice.api.dto.request.CreateExportInvoiceRequest;
import com.ttdat.salesservice.application.services.ExportInvoiceService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/export-invoices")
@RequiredArgsConstructor
public class ExportInvoiceCommandController {
    private final ExportInvoiceService exportInvoiceService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ApiResponse<Object> createImportInvoice(@RequestBody @Valid CreateExportInvoiceRequest createExportInvoiceRequest) {
        exportInvoiceService.createExportInvoice(createExportInvoiceRequest);
        return ApiResponse.builder()
                .status(HttpStatus.CREATED.value())
                .message("Import invoice created successfully")
                .success(true)
                .build();
    }

}
