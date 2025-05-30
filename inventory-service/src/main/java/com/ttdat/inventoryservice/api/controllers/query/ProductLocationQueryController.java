package com.ttdat.inventoryservice.api.controllers.query;

import com.ttdat.core.api.dto.request.PaginationParams;
import com.ttdat.core.api.dto.request.SortParams;
import com.ttdat.core.api.dto.response.ApiResponse;
import com.ttdat.core.infrastructure.utils.RequestParamsUtils;
import com.ttdat.inventoryservice.api.dto.common.ProductLocationDTO;
import com.ttdat.inventoryservice.api.dto.response.ProductLocationPageResult;
import com.ttdat.inventoryservice.application.queries.location.GetAllProductLocationQuery;
import com.ttdat.inventoryservice.application.queries.location.GetProductLocationPageQuery;
import com.ttdat.inventoryservice.application.queries.location.SearchProductLocationQuery;
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
@RequestMapping("/api/v1/product-locations")
@RequiredArgsConstructor
public class ProductLocationQueryController {
    private final QueryGateway queryGateway;

    @GetMapping("/page")
    public ApiResponse<ProductLocationPageResult> getProductLocationPage(@RequestParam Map<String, String> filterParams) {
        PaginationParams paginationParams = RequestParamsUtils.getPaginationParams(filterParams);
        SortParams sortParams = RequestParamsUtils.getSortParams(filterParams);
        GetProductLocationPageQuery getProductLocationPageQuery = GetProductLocationPageQuery.builder()
                .paginationParams(paginationParams)
                .sortParams(sortParams)
                .filterParams(filterParams)
                .build();
        ProductLocationPageResult productLocationPage = queryGateway.query(getProductLocationPageQuery, ResponseTypes.instanceOf(ProductLocationPageResult.class)).join();
        return ApiResponse.<ProductLocationPageResult>builder()
                .status(HttpStatus.OK.value())
                .success(true)
                .message("Product location page fetched successfully")
                .payload(productLocationPage)
                .build();
    }

    @GetMapping
    public ApiResponse<List<ProductLocationDTO>> getProductLocations() {
        GetAllProductLocationQuery getAllProductLocationQuery = GetAllProductLocationQuery.builder().build();
        List<ProductLocationDTO> productLocations = queryGateway.query(getAllProductLocationQuery, ResponseTypes.multipleInstancesOf(ProductLocationDTO.class)).join();
        return ApiResponse.<List<ProductLocationDTO>>builder()
                .status(HttpStatus.OK.value())
                .success(true)
                .message("Product locations fetched successfully")
                .payload(productLocations)
                .build();
    }

    @GetMapping("/search")
    public ApiResponse<List<ProductLocationDTO>> searchProductLocations(@RequestParam String query) {
        SearchProductLocationQuery searchProductLocationQuery = SearchProductLocationQuery.builder()
                .query(query)
                .build();
        List<ProductLocationDTO> productLocations = queryGateway.query(searchProductLocationQuery, ResponseTypes.multipleInstancesOf(ProductLocationDTO.class)).join();
        return ApiResponse.<List<ProductLocationDTO>>builder()
                .status(HttpStatus.OK.value())
                .success(true)
                .message("Product locations fetched successfully")
                .payload(productLocations)
                .build();
    }
}
