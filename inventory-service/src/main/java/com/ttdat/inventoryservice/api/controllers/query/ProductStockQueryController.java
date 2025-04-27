package com.ttdat.inventoryservice.api.controllers.query;

import com.ttdat.core.api.dto.request.PaginationParams;
import com.ttdat.core.api.dto.request.SortParams;
import com.ttdat.core.api.dto.response.ApiResponse;
import com.ttdat.core.infrastructure.utils.RequestParamsUtils;
import com.ttdat.inventoryservice.api.dto.common.ProductBatchDTO;
import com.ttdat.inventoryservice.api.dto.common.ProductBatchLocationDTO;
import com.ttdat.inventoryservice.api.dto.common.ProductStockDTO;
import com.ttdat.inventoryservice.api.dto.response.CategoryInventoryChartData;
import com.ttdat.inventoryservice.api.dto.response.ProductStockPageResult;
import com.ttdat.inventoryservice.application.queries.stock.GetProductStockPageQuery;
import com.ttdat.inventoryservice.application.queries.stock.GetWarehouseProductStockQuery;
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
import java.util.stream.Collectors;

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

    @GetMapping("/stats/category-inventory")
    public ApiResponse<List<CategoryInventoryChartData>> getCategoryInventoryChartData() {
        GetWarehouseProductStockQuery getWarehouseProductStockQuery = GetWarehouseProductStockQuery.builder()
                .build();
        List<ProductStockDTO> productStocks = queryGateway.query(getWarehouseProductStockQuery, ResponseTypes.multipleInstancesOf(ProductStockDTO.class)).join();
        Map<String, Double> categoryInventoryMap = productStocks.stream()
                .collect(Collectors.groupingBy(
                        productStock -> productStock.getProduct().getCategory().getCategoryName(),
                        Collectors.summingDouble(ProductStockDTO::getQuantity)
                ));
        List<CategoryInventoryChartData> categoryInventoryChartDataList = categoryInventoryMap.entrySet().stream()
                .map(entry -> CategoryInventoryChartData.builder()
                        .label(entry.getKey())
                        .value(entry.getValue())
                        .build())
                .collect(Collectors.toList());
        return ApiResponse.<List<CategoryInventoryChartData>>builder()
                .status(HttpStatus.OK.value())
                .success(true)
                .message("Category inventory chart data fetched successfully")
                .payload(categoryInventoryChartDataList)
                .build();


    }
}
