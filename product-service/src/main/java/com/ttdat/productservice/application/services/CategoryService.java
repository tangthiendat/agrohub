package com.ttdat.productservice.application.services;

import com.ttdat.productservice.api.dto.common.CategoryDTO;

public interface CategoryService {
    void createCategory(CategoryDTO categoryDTO);

    void updateCategory(Long id, CategoryDTO categoryDTO);
}
