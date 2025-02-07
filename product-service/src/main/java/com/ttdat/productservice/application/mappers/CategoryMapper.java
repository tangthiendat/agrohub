package com.ttdat.productservice.application.mappers;

import com.ttdat.productservice.api.dto.common.CategoryDTO;
import com.ttdat.productservice.domain.entities.Category;
import org.mapstruct.InjectionStrategy;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING,
        injectionStrategy = InjectionStrategy.CONSTRUCTOR)
public interface CategoryMapper extends EntityMapper<CategoryDTO, Category> {
}
