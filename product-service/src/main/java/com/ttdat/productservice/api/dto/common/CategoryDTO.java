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
public class CategoryDTO {
    Long categoryId;

    @NotBlank(message = "Category name is required")
    String categoryName;

    LocalDateTime createdAt;

    LocalDateTime updatedAt;
}
