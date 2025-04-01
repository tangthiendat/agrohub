package com.ttdat.customerservice.api.controllers.command;

import com.ttdat.core.api.dto.response.ApiResponse;
import com.ttdat.customerservice.api.dto.CustomerDTO;
import com.ttdat.customerservice.application.services.CustomerService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/customers")
@RequiredArgsConstructor
public class CustomerCommandController {
    private final CustomerService customerService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ApiResponse<Object> create(@RequestBody @Valid CustomerDTO customerDTO) {
        customerService.createCustomer(customerDTO);
        return ApiResponse.builder()
                .status(HttpStatus.CREATED.value())
                .success(true)
                .message("Customer created successfully")
                .build();
    }

    @PutMapping("/{id}")
    public ApiResponse<Object> update(@PathVariable String id, @RequestBody @Valid CustomerDTO customerDTO) {
        customerService.updateCustomer(id, customerDTO);
        return ApiResponse.builder()
                .status(HttpStatus.OK.value())
                .success(true)
                .message("Customer updated successfully")
                .build();
    }
}
