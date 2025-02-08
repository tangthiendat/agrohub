package com.ttdat.productservice.application.services.impl;

import com.ttdat.productservice.api.dto.common.CategoryDTO;
import com.ttdat.productservice.application.commands.category.CreateCategoryCommand;
import com.ttdat.productservice.application.mappers.CategoryMapper;
import com.ttdat.productservice.application.services.CategoryService;
import lombok.RequiredArgsConstructor;
import org.axonframework.commandhandling.gateway.CommandGateway;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CategoryServiceImpl implements CategoryService {
    private final CommandGateway commandGateway;
    private final CategoryMapper categoryMapper;

    @Override
    public void createCategory(CategoryDTO categoryDTO) {
        CreateCategoryCommand createCategoryCommand = categoryMapper.toCommand(categoryDTO);
        commandGateway.sendAndWait(createCategoryCommand);
    }
}
