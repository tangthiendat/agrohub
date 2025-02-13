package com.ttdat.productservice.api.controllers.command;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ttdat.core.api.dto.response.ApiResponse;
import com.ttdat.productservice.api.dto.common.ProductDTO;
import com.ttdat.productservice.application.services.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@RequestMapping("/api/v1/products")
@RequiredArgsConstructor
public class ProductCommandController {
    private final ProductService productService;
    private final ObjectMapper objectMapper;

    @PostMapping(consumes = {MediaType.MULTIPART_FORM_DATA_VALUE})
    public ResponseEntity<ApiResponse<Object>> createProduct(@RequestPart("product") String productJson,
                                                             @RequestPart("product_img") MultipartFile productImg) throws IOException {
        ProductDTO productDTO = objectMapper.readValue(productJson, ProductDTO.class);
        productService.createProduct(productDTO, productImg);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.builder()
                        .status(HttpStatus.CREATED.value())
                        .success(true)
                        .message("Product created successfully")
                        .build());
    }
}
