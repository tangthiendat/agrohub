package com.ttdat.productservice.application.mappers;

import com.ttdat.productservice.api.dto.common.CategoryDTO;
import com.ttdat.productservice.application.commands.category.CreateCategoryCommand;
import com.ttdat.productservice.domain.entities.Category;
import com.ttdat.productservice.domain.events.category.CategoryCreatedEvent;
import org.mapstruct.InjectionStrategy;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING,
        injectionStrategy = InjectionStrategy.CONSTRUCTOR)
public interface CategoryMapper extends EntityMapper<CategoryDTO, Category> {
    CreateCategoryCommand toCommand(CategoryDTO categoryDTO);

    @Mapping(source = "id", target = "categoryId")
    CategoryCreatedEvent toEvent(Long id, CreateCategoryCommand createCategoryCommand);

    Category toEntity(CategoryCreatedEvent categoryCreatedEvent);
}
