package com.ttdat.inventoryservice.api.controllers.query;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/warehouses")
@RequiredArgsConstructor
public class WarehouseQueryController {

    @GetMapping("/page")
    public String getWarehousePage() {
        return "Get warehouse page successfully";
    }
}
