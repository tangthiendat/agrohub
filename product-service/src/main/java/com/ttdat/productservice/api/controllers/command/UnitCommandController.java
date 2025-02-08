package com.ttdat.productservice.api.controllers.command;

import com.ttdat.core.api.dto.response.ApiResponse;
import com.ttdat.productservice.api.dto.common.UnitDTO;
import com.ttdat.productservice.application.services.UnitService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/units")
@RequiredArgsConstructor
public class UnitCommandController {
    private final UnitService unitService;

    @PostMapping
    public ApiResponse<Object> createUnit(@Valid @RequestBody UnitDTO unitDTO){
        unitService.createUnit(unitDTO);
        return ApiResponse.builder()
                .status(HttpStatus.CREATED.value())
                .success(true)
                .message("Unit created successfully")
                .build();
    }

    @PutMapping("/{id}")
    public ApiResponse<Object> updateUnit(@PathVariable Long id, @Valid @RequestBody UnitDTO unitDTO){
        unitService.updateUnit(id, unitDTO);
        return ApiResponse.builder()
                .status(HttpStatus.OK.value())
                .success(true)
                .message("Unit updated successfully")
                .build();
    }
}
