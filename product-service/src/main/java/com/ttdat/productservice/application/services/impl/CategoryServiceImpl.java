package com.ttdat.productservice.application.services.impl;

import com.ttdat.productservice.api.dto.common.CategoryDTO;
import com.ttdat.productservice.application.commands.category.CreateCategoryCommand;
import com.ttdat.productservice.application.commands.category.UpdateCategoryCommand;
import com.ttdat.productservice.application.mappers.CategoryMapper;
import com.ttdat.productservice.application.services.CategoryService;
import com.ttdat.productservice.infrastructure.services.IdGeneratorService;
import lombok.RequiredArgsConstructor;
import org.axonframework.commandhandling.gateway.CommandGateway;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CategoryServiceImpl implements CategoryService {
    private final CommandGateway commandGateway;
    private final CategoryMapper categoryMapper;
    private final IdGeneratorService idGeneratorService;

    @Override
    public void createCategory(CategoryDTO categoryDTO) {
        Long id = idGeneratorService.generateCategoryId();
        CreateCategoryCommand createCategoryCommand = categoryMapper.toCreateCommand(id, categoryDTO);
        commandGateway.sendAndWait(createCategoryCommand);
    }

    @Override
    public void updateCategory(Long id, CategoryDTO categoryDTO) {
        UpdateCategoryCommand updateCategoryCommand = categoryMapper.toUpdateCommand(id, categoryDTO);
        commandGateway.sendAndWait(updateCategoryCommand);
    }
}
