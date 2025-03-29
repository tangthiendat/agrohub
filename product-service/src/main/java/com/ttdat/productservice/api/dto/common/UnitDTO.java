package com.ttdat.productservice.api.dto.common;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDateTime;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UnitDTO {
    Long unitId;

    @NotBlank(message = "Unit name is required")
    @Size(max = 50, message = "Unit name must be less than 50 characters")
    String unitName;

    @Size(max = 255, message = "Description must be less than 255 characters")
    String description;

    LocalDateTime createdAt;

    LocalDateTime updatedAt;
}
