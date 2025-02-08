package com.ttdat.productservice.api.controllers.query;

import com.ttdat.core.api.dto.response.ApiResponse;
import com.ttdat.productservice.api.dto.common.CategoryDTO;
import com.ttdat.productservice.application.queries.category.GetAllCategoriesQuery;
import lombok.RequiredArgsConstructor;
import org.axonframework.messaging.responsetypes.ResponseTypes;
import org.axonframework.queryhandling.QueryGateway;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/v1/categories")
@RequiredArgsConstructor
public class CategoryQueryController {
    private final QueryGateway queryGateway;

    @GetMapping
    public ResponseEntity<ApiResponse<List<CategoryDTO>>> getCategories() {
        GetAllCategoriesQuery getAllCategoriesQuery = GetAllCategoriesQuery.builder().build();
        List<CategoryDTO> categories = queryGateway.query(getAllCategoriesQuery, ResponseTypes.multipleInstancesOf(CategoryDTO.class)).join();
        return ResponseEntity.status(HttpStatus.OK)
                .body(ApiResponse.<List<CategoryDTO>>builder()
                        .status(HttpStatus.OK.value())
                        .success(true)
                        .payload(categories)
                        .build());
    }
}
