package com.ttdat.productservice.api.dto.common;

import jakarta.validation.constraints.NotBlank;
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
    String unitName;

    String description;

    LocalDateTime createdAt;

    LocalDateTime updatedAt;
}
