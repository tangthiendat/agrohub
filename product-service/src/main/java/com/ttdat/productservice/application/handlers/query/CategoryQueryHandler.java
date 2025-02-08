package com.ttdat.productservice.application.handlers.query;

import com.ttdat.productservice.api.dto.common.CategoryDTO;
import com.ttdat.productservice.application.mappers.CategoryMapper;
import com.ttdat.productservice.application.queries.category.GetAllCategoriesQuery;
import com.ttdat.productservice.domain.repositories.CategoryRepository;
import lombok.RequiredArgsConstructor;
import org.axonframework.queryhandling.QueryHandler;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class CategoryQueryHandler {
    private final CategoryRepository categoryRepository;
    private final CategoryMapper categoryMapper;

    @QueryHandler
    public List<CategoryDTO> handle(GetAllCategoriesQuery getAllCategoriesQuery) {
        return categoryMapper.toDTOs(categoryRepository.findAll());
    }
}
