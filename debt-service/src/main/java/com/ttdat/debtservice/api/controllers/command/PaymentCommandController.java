package com.ttdat.debtservice.api.controllers.command;

import com.ttdat.core.api.dto.response.ApiResponse;
import com.ttdat.debtservice.api.dto.request.CreatePaymentRequest;
import com.ttdat.debtservice.application.services.PaymentService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/payments")
@RequiredArgsConstructor
public class PaymentCommandController {
    private final PaymentService paymentService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ApiResponse<Object> createPayment(@RequestBody @Valid CreatePaymentRequest createPaymentRequest){
        paymentService.createPayment(createPaymentRequest);
        return ApiResponse.builder()
                .message("Payment created successfully")
                .success(true)
                .status(HttpStatus.CREATED.value())
                .build();
    }
}
