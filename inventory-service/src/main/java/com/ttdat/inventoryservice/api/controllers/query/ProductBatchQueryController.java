package com.ttdat.inventoryservice.api.controllers.query;

import com.ttdat.core.api.dto.request.PaginationParams;
import com.ttdat.core.api.dto.request.SortParams;
import com.ttdat.core.api.dto.response.ApiResponse;
import com.ttdat.core.infrastructure.utils.RequestParamsUtils;
import com.ttdat.inventoryservice.api.dto.common.ProductBatchDTO;
import com.ttdat.inventoryservice.api.dto.response.ProductBatchPageResult;
import com.ttdat.inventoryservice.application.queries.batch.GetAllProductBatchQuery;
import com.ttdat.inventoryservice.application.queries.batch.GetProductBatchPageQuery;
import lombok.RequiredArgsConstructor;
import org.axonframework.messaging.responsetypes.ResponseTypes;
import org.axonframework.queryhandling.QueryGateway;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/product-batches")
@RequiredArgsConstructor
public class ProductBatchQueryController {
    private final QueryGateway queryGateway;

    @GetMapping("/page")
    public ApiResponse<ProductBatchPageResult> getProductBatchPage(@RequestParam Map<String, String> filterParams) {
        PaginationParams paginationParams = RequestParamsUtils.getPaginationParams(filterParams);
        SortParams sortParams = RequestParamsUtils.getSortParams(filterParams);
        GetProductBatchPageQuery getProductBatchPageQuery = GetProductBatchPageQuery.builder()
                .paginationParams(paginationParams)
                .sortParams(sortParams)
                .filterParams(filterParams)
                .build();
        ProductBatchPageResult productBatchPage = queryGateway.query(getProductBatchPageQuery, ProductBatchPageResult.class).join();
        return ApiResponse.<ProductBatchPageResult>builder()
                .status(HttpStatus.OK.value())
                .success(true)
                .message("Product batch page fetched successfully")
                .payload(productBatchPage)
                .build();
    }

    @GetMapping
    public ApiResponse<List<ProductBatchDTO>> getProductBatches(@RequestParam Map<String, String> filterParams) {
        GetAllProductBatchQuery getAllProductBatchQuery = GetAllProductBatchQuery.builder().filterParams(filterParams).build();
        List<ProductBatchDTO> productBatches = queryGateway.query(getAllProductBatchQuery, ResponseTypes.multipleInstancesOf(ProductBatchDTO.class)).join();
        return ApiResponse.<List<ProductBatchDTO>>builder()
                .status(HttpStatus.OK.value())
                .success(true)
                .message("Product batches fetched successfully")
                .payload(productBatches)
                .build();
    }
}
