package com.ttdat.customerservice.api.controllers.query;

import com.ttdat.core.api.dto.request.PaginationParams;
import com.ttdat.core.api.dto.request.SortParams;
import com.ttdat.core.api.dto.response.ApiResponse;
import com.ttdat.core.infrastructure.utils.RequestParamsUtils;
import com.ttdat.customerservice.api.dto.CustomerPageResult;
import com.ttdat.customerservice.application.queries.customer.GetCustomerQueryPageQuery;
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
@RequestMapping("/api/v1/customers")
@RequiredArgsConstructor
public class CustomerQueryController {
    private final QueryGateway queryGateway;

    @GetMapping("/page")
    public ApiResponse<CustomerPageResult> getCustomerPage(@RequestParam Map<String, String> filterParams) {
        PaginationParams paginationParams = RequestParamsUtils.getPaginationParams(filterParams);
        SortParams sortParams = RequestParamsUtils.getSortParams(filterParams);
        GetCustomerQueryPageQuery getCustomerQueryPageQuery = GetCustomerQueryPageQuery.builder()
                .paginationParams(paginationParams)
                .sortParams(sortParams)
                .filterParams(filterParams)
                .build();
        CustomerPageResult customerPageResult = queryGateway.query(getCustomerQueryPageQuery, ResponseTypes.instanceOf(CustomerPageResult.class)).join();
        return ApiResponse.<CustomerPageResult>builder()
                .status(HttpStatus.OK.value())
                .success(true)
                .message("Customer page retrieved successfully")
                .payload(customerPageResult)
                .build();
    }
}
