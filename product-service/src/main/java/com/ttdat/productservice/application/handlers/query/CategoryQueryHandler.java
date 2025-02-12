package com.ttdat.productservice.application.handlers.query;

import com.ttdat.core.api.dto.response.PaginationMeta;
import com.ttdat.productservice.api.dto.common.CategoryDTO;
import com.ttdat.productservice.api.dto.response.CategoryPageResult;
import com.ttdat.productservice.application.mappers.CategoryMapper;
import com.ttdat.productservice.application.queries.category.GetAllCategoriesQuery;
import com.ttdat.productservice.application.queries.category.GetCategoryPageQuery;
import com.ttdat.productservice.domain.entities.Category;
import com.ttdat.productservice.domain.repositories.CategoryRepository;
import com.ttdat.productservice.infrastructure.utils.PaginationUtils;
import lombok.RequiredArgsConstructor;
import org.axonframework.queryhandling.QueryHandler;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class CategoryQueryHandler {
    private final CategoryRepository categoryRepository;
    private final CategoryMapper categoryMapper;

    @QueryHandler
    public List<CategoryDTO> handle(GetAllCategoriesQuery getAllCategoriesQuery) {
        return categoryMapper.toDTOList(categoryRepository.findAll());
    }

    @QueryHandler
    public CategoryPageResult handle(GetCategoryPageQuery getCategoryPageQuery) {
        Pageable pageable = PaginationUtils.getPageable(getCategoryPageQuery.getPaginationParams(), getCategoryPageQuery.getSortParams());
        Page<Category> categoryPage = categoryRepository.findAll(pageable);
        PaginationMeta paginationMeta = PaginationUtils.getPaginationMeta(categoryPage);
        return CategoryPageResult.builder()
                .meta(paginationMeta)
                .content(categoryMapper.toDTOList(categoryPage.getContent()))
                .build();
    }
}
