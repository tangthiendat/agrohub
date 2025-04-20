package com.ttdat.customerservice.api.controllers.query;

import com.ttdat.core.api.dto.request.PaginationParams;
import com.ttdat.core.api.dto.request.SortParams;
import com.ttdat.core.api.dto.response.ApiResponse;
import com.ttdat.core.infrastructure.utils.RequestParamsUtils;
import com.ttdat.customerservice.api.dto.CustomerDTO;
import com.ttdat.customerservice.api.dto.response.CustomerPageResult;
import com.ttdat.customerservice.application.queries.customer.GetCustomerByIdQuery;
import com.ttdat.customerservice.application.queries.customer.GetCustomerQueryPageQuery;
import com.ttdat.customerservice.application.queries.customer.SearchCustomerQuery;
import lombok.RequiredArgsConstructor;
import org.axonframework.messaging.responsetypes.ResponseTypes;
import org.axonframework.queryhandling.QueryGateway;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;
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

    @GetMapping("/search")
    public ApiResponse<List<CustomerDTO>> searchCustomer(@RequestParam String query){
        SearchCustomerQuery searchCustomerQuery = SearchCustomerQuery.builder()
                .query(query)
                .build();
        List<CustomerDTO> customerDTOList = queryGateway.query(searchCustomerQuery, ResponseTypes.multipleInstancesOf(CustomerDTO.class)).join();
        return ApiResponse.<List<CustomerDTO>>builder()
                .status(HttpStatus.OK.value())
                .success(true)
                .message("Customer search results retrieved successfully")
                .payload(customerDTOList)
                .build();
    }

    @GetMapping("/{id}")
    public ApiResponse<CustomerDTO> getCustomerById(@PathVariable String id) {
        GetCustomerByIdQuery getCustomerByIdQuery = GetCustomerByIdQuery.builder()
                .customerId(id)
                .build();
        CustomerDTO customerDTO = queryGateway.query(getCustomerByIdQuery, ResponseTypes.instanceOf(CustomerDTO.class)).join();
        return ApiResponse.<CustomerDTO>builder()
                .status(HttpStatus.OK.value())
                .success(true)
                .message("Customer retrieved successfully")
                .payload(customerDTO)
                .build();
    }
}
