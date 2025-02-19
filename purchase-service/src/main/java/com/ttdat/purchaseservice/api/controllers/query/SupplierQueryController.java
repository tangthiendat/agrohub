package com.ttdat.purchaseservice.api.controllers.query;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/suppliers")
@RequiredArgsConstructor
public class SupplierQueryController {

    @GetMapping("/page")
    public String getSupplierPage() {
        return "Supplier page retrieved successfully";
    }
}
