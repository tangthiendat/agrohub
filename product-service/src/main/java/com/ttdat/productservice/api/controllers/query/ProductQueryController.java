package com.ttdat.productservice.api.controllers.query;

import com.ttdat.core.api.dto.request.PaginationParams;
import com.ttdat.core.api.dto.request.SortParams;
import com.ttdat.core.api.dto.response.ApiResponse;
import com.ttdat.core.infrastructure.utils.RequestParamsUtils;
import com.ttdat.productservice.api.dto.common.ProductDTO;
import com.ttdat.productservice.api.dto.response.ProductPageResult;
import com.ttdat.productservice.application.queries.product.GetProductByIdQuery;
import com.ttdat.productservice.application.queries.product.GetProductPageQuery;
import com.ttdat.productservice.application.queries.product.SearchProductQuery;
import lombok.RequiredArgsConstructor;
import org.axonframework.messaging.responsetypes.ResponseTypes;
import org.axonframework.queryhandling.QueryGateway;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/products")
@RequiredArgsConstructor
public class ProductQueryController {
    private final QueryGateway queryGateway;

    @GetMapping("/page")
    public ApiResponse<ProductPageResult> getProductPage(@RequestParam Map<String, String> filterParams) {
        PaginationParams paginationParams = RequestParamsUtils.getPaginationParams(filterParams);
        SortParams sortParams = RequestParamsUtils.getSortParams(filterParams);
        GetProductPageQuery getProductPageQuery = GetProductPageQuery.builder()
                .paginationParams(paginationParams)
                .sortParams(sortParams)
                .filterParams(filterParams)
                .build();
        ProductPageResult productPageResult = queryGateway.query(getProductPageQuery, ResponseTypes.instanceOf(ProductPageResult.class)).join();
        return ApiResponse.<ProductPageResult>builder()
                .status(HttpStatus.OK.value())
                .message("Product page retrieved successfully")
                .success(true)
                .payload(productPageResult)
                .build();
    }

    @GetMapping("/{id}")
    public ApiResponse<ProductDTO> getProductById(@PathVariable String id) {
        GetProductByIdQuery getProductByIdQuery = GetProductByIdQuery.builder()
                .productId(id)
                .build();
        ProductDTO productDTO = queryGateway.query(getProductByIdQuery, ResponseTypes.instanceOf(ProductDTO.class)).join();
        return ApiResponse.<ProductDTO>builder()
                .status(HttpStatus.OK.value())
                .message("Product retrieved successfully")
                .success(true)
                .payload(productDTO)
                .build();
    }

    @GetMapping("/search")
    public ApiResponse<List<ProductDTO>> getAllProducts(@RequestParam String query) {
        SearchProductQuery searchProductQuery = SearchProductQuery.builder()
                .query(query)
                .build();
        List<ProductDTO> productDTOs = queryGateway.query(searchProductQuery, ResponseTypes.multipleInstancesOf(ProductDTO.class)).join();
        return ApiResponse.<List<ProductDTO>>builder()
                .status(HttpStatus.OK.value())
                .message("Products retrieved successfully")
                .success(true)
                .payload(productDTOs)
                .build();
    }
}
