package com.ttdat.productservice.api.controllers.command;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ttdat.core.api.dto.response.ApiResponse;
import com.ttdat.productservice.api.dto.common.ProductDTO;
import com.ttdat.productservice.application.services.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@RequestMapping("/api/v1/products")
@RequiredArgsConstructor
public class ProductCommandController {
    private final ProductService productService;
    private final ObjectMapper objectMapper;

    @PostMapping(consumes = {MediaType.MULTIPART_FORM_DATA_VALUE})
    @ResponseStatus(HttpStatus.CREATED)
    public ApiResponse<Object> createProduct(@RequestPart("product") String productJson,
                                             @RequestPart("product_img") MultipartFile productImg) throws IOException {
        ProductDTO productDTO = objectMapper.readValue(productJson, ProductDTO.class);
        productService.createProduct(productDTO, productImg);
        return ApiResponse.builder()
                .status(HttpStatus.CREATED.value())
                .success(true)
                .message("Product created successfully")
                .build();
    }

    @PutMapping(value = "/{id}", consumes = {MediaType.MULTIPART_FORM_DATA_VALUE})
    public ApiResponse<Object> updateProduct(@PathVariable("id") String productId,
                                             @RequestPart("product") String productJson,
                                             @RequestPart(value = "product_img", required = false) MultipartFile productImg) throws IOException {
        ProductDTO productDTO = objectMapper.readValue(productJson, ProductDTO.class);
        productService.updateProduct(productId, productDTO, productImg);
        return ApiResponse.builder()
                .status(HttpStatus.OK.value())
                .success(true)
                .message("Product updated successfully")
                .build();
    }

}
