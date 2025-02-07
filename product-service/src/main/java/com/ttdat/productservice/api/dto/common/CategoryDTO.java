package com.ttdat.productservice.api.dto.common;

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

    String categoryName;

    LocalDateTime createdAt;

    LocalDateTime updatedAt;
}
