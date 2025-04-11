package com.ttdat.salesservice.api.controllers.command;

import com.ttdat.core.api.dto.response.ApiResponse;
import com.ttdat.salesservice.api.dto.request.CreateExportInvoiceRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/export-invoices")
@RequiredArgsConstructor
public class ExportInvoiceCommandController {

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ApiResponse<CreateExportInvoiceRequest> createImportInvoice(@RequestBody @Valid CreateExportInvoiceRequest createExportInvoiceRequest) {
        return ApiResponse.<CreateExportInvoiceRequest>builder()
                .status(HttpStatus.CREATED.value())
                .message("Import invoice created successfully")
                .payload(createExportInvoiceRequest)
                .success(true)
                .build();
    }

}
