package com.ttdat.inventoryservice.api.controllers.query;

import com.ttdat.core.api.dto.request.PaginationParams;
import com.ttdat.core.api.dto.request.SortParams;
import com.ttdat.core.api.dto.response.ApiResponse;
import com.ttdat.core.infrastructure.utils.RequestParamsUtils;
import com.ttdat.inventoryservice.api.dto.response.ProductStockPageResult;
import com.ttdat.inventoryservice.application.queries.stock.GetProductStockPageQuery;
import lombok.RequiredArgsConstructor;
import org.axonframework.messaging.responsetypes.ResponseTypes;
import org.axonframework.queryhandling.QueryGateway;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/api/v1/product-stocks")
@RequiredArgsConstructor
public class ProductStockQueryController {
    private final QueryGateway queryGateway;

    @GetMapping("/page")
    public ApiResponse<ProductStockPageResult> getProductStockPage(@RequestParam Map<String, String> filterParams) {
        PaginationParams paginationParams = RequestParamsUtils.getPaginationParams(filterParams);
        SortParams sortParams = RequestParamsUtils.getSortParams(filterParams);
        GetProductStockPageQuery getProductStockPageQuery = GetProductStockPageQuery.builder()
                .paginationParams(paginationParams)
                .sortParams(sortParams)
                .filterParams(filterParams)
                .build();
        ProductStockPageResult productStockPage = queryGateway.query(getProductStockPageQuery, ResponseTypes.instanceOf(ProductStockPageResult.class)).join();
        return ApiResponse.<ProductStockPageResult>builder()
                .status(HttpStatus.OK.value())
                .success(true)
                .message("Product stock page fetched successfully")
                .payload(productStockPage)
                .build();
    }
}
