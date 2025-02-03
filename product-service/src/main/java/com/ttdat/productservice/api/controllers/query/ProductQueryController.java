package com.ttdat.productservice.api.controllers.query;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/product")
@RequiredArgsConstructor
public class ProductQueryController {

    @GetMapping("/page")
    public String getProducts() {
        return "Get product page";
    }
}
