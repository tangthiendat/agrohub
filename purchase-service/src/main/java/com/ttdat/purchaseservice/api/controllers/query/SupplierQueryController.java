package com.ttdat.purchaseservice.api.controllers.query;

import com.ttdat.core.api.dto.request.PaginationParams;
import com.ttdat.core.api.dto.request.SortParams;
import com.ttdat.core.api.dto.response.ApiResponse;
import com.ttdat.core.infrastructure.utils.RequestParamsUtils;
import com.ttdat.purchaseservice.api.dto.common.SupplierDTO;
import com.ttdat.purchaseservice.api.dto.response.SupplierPageResult;
import com.ttdat.purchaseservice.application.queries.supplier.GetSupplierByIdQuery;
import com.ttdat.purchaseservice.application.queries.supplier.GetSupplierPageQuery;
import com.ttdat.purchaseservice.application.queries.supplier.SearchSupplierQuery;
import lombok.RequiredArgsConstructor;
import org.axonframework.messaging.responsetypes.ResponseTypes;
import org.axonframework.queryhandling.QueryGateway;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/suppliers")
@RequiredArgsConstructor
public class SupplierQueryController {
    private final QueryGateway queryGateway;

    @GetMapping("/page")
    public ApiResponse<SupplierPageResult> getSupplierPage(@RequestParam Map<String, String> filterParams) {
        PaginationParams paginationParams = RequestParamsUtils.getPaginationParams(filterParams);
        SortParams sortParams = RequestParamsUtils.getSortParams(filterParams);
        GetSupplierPageQuery getSupplierPageQuery = GetSupplierPageQuery.builder()
                .paginationParams(paginationParams)
                .sortParams(sortParams)
                .filterParams(filterParams)
                .build();
        SupplierPageResult supplierPageResult = queryGateway.query(getSupplierPageQuery, ResponseTypes.instanceOf(SupplierPageResult.class)).join();
        return ApiResponse.<SupplierPageResult>builder()
                .status(HttpStatus.OK.value())
                .message("Get supplier page successfully")
                .success(true)
                .payload(supplierPageResult)
                .build();
    }

    @GetMapping("/search")
    public ApiResponse<List<SupplierDTO>> searchSuppliers(@RequestParam String query) {
        SearchSupplierQuery searchSupplierQuery = SearchSupplierQuery.builder()
                .query(query)
                .build();
        List<SupplierDTO> supplierDTOS = queryGateway.query(searchSupplierQuery, ResponseTypes.multipleInstancesOf(SupplierDTO.class)).join();
        return ApiResponse.<List<SupplierDTO>>builder()
                .status(HttpStatus.OK.value())
                .message("Search suppliers successfully")
                .success(true)
                .payload(supplierDTOS)
                .build();
    }

    @GetMapping("/{id}")
    public ApiResponse<SupplierDTO> getSupplierById(@PathVariable String id) {
        GetSupplierByIdQuery getSupplierByIdQuery = GetSupplierByIdQuery.builder()
                .supplierId(id)
                .build();
        SupplierDTO supplierDTO = queryGateway.query(getSupplierByIdQuery, ResponseTypes.instanceOf(SupplierDTO.class)).join();
        return ApiResponse.<SupplierDTO>builder()
                .status(HttpStatus.OK.value())
                .message("Get supplier by id successfully")
                .success(true)
                .payload(supplierDTO)
                .build();
    }
}
