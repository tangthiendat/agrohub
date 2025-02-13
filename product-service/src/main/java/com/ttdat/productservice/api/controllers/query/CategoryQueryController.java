package com.ttdat.productservice.api.controllers.query;

import com.ttdat.core.api.dto.request.PaginationParams;
import com.ttdat.core.api.dto.request.SortParams;
import com.ttdat.core.api.dto.response.ApiResponse;
import com.ttdat.core.infrastructure.utils.RequestParamsUtils;
import com.ttdat.productservice.api.dto.common.CategoryDTO;
import com.ttdat.productservice.api.dto.response.CategoryPageResult;
import com.ttdat.productservice.application.queries.category.GetAllCategoriesQuery;
import com.ttdat.productservice.application.queries.category.GetCategoryPageQuery;
import lombok.RequiredArgsConstructor;
import org.axonframework.messaging.responsetypes.ResponseTypes;
import org.axonframework.queryhandling.QueryGateway;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

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

    @GetMapping("/page")
    public ResponseEntity<ApiResponse<CategoryPageResult>> getCategoryPage(@RequestParam Map<String, String> filterParams) {
        PaginationParams paginationParams = RequestParamsUtils.getPaginationParams(filterParams);
        SortParams sortParams = RequestParamsUtils.getSortParams(filterParams);
        GetCategoryPageQuery getCategoryPageQuery = GetCategoryPageQuery.builder()
                .paginationParams(paginationParams)
                .sortParams(sortParams)
                .build();
        CategoryPageResult categoryPageResult = queryGateway.query(getCategoryPageQuery, ResponseTypes.instanceOf(CategoryPageResult.class)).join();
        return ResponseEntity.status(HttpStatus.OK)
                .body(ApiResponse.<CategoryPageResult>builder()
                        .status(HttpStatus.OK.value())
                        .success(true)
                        .payload(categoryPageResult)
                        .build());
    }
}
