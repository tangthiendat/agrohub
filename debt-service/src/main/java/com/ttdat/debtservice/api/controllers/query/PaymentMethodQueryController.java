package com.ttdat.debtservice.api.controllers.query;

import com.ttdat.core.api.dto.request.PaginationParams;
import com.ttdat.core.api.dto.request.SortParams;
import com.ttdat.core.api.dto.response.ApiResponse;
import com.ttdat.core.infrastructure.utils.RequestParamsUtils;
import com.ttdat.debtservice.api.dto.common.PaymentMethodDTO;
import com.ttdat.debtservice.api.dto.response.PaymentMethodPageResult;
import com.ttdat.debtservice.application.queries.paymentmethod.GetAllPaymentMethodQuery;
import com.ttdat.debtservice.application.queries.paymentmethod.GetPaymentMethodPageQuery;
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
@RequestMapping("/api/v1/payment-methods")
@RequiredArgsConstructor
public class PaymentMethodQueryController {
    private final QueryGateway queryGateway;

    @GetMapping("/page")
    public ApiResponse<PaymentMethodPageResult> getPaymentMethodPage(@RequestParam Map<String, String> filterParams) {
        PaginationParams paginationParams = RequestParamsUtils.getPaginationParams(filterParams);
        SortParams sortParams = RequestParamsUtils.getSortParams(filterParams);
        GetPaymentMethodPageQuery getPaymentMethodPageQuery = GetPaymentMethodPageQuery.builder()
                .paginationParams(paginationParams)
                .sortParams(sortParams)
                .build();
        PaymentMethodPageResult paymentMethodPageResult = queryGateway.query(getPaymentMethodPageQuery, ResponseTypes.instanceOf(PaymentMethodPageResult.class)).join();
        return ApiResponse.<PaymentMethodPageResult>builder()
                .status(HttpStatus.OK.value())
                .success(true)
                .message("Payment method page retrieved successfully")
                .payload(paymentMethodPageResult)
                .build();
    }

    @GetMapping
    public ApiResponse<List<PaymentMethodDTO>> getAllPaymentMethods() {
        GetAllPaymentMethodQuery getAllPaymentMethodQuery = GetAllPaymentMethodQuery.builder().build();
        List<PaymentMethodDTO> paymentMethods = queryGateway.query(getAllPaymentMethodQuery, ResponseTypes.multipleInstancesOf(PaymentMethodDTO.class)).join();
        return ApiResponse.<List<PaymentMethodDTO>>builder()
                .status(HttpStatus.OK.value())
                .success(true)
                .message("All payment methods retrieved successfully")
                .payload(paymentMethods)
                .build();
    }
}
