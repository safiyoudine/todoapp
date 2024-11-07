package com.todo.mapper;

import com.todo.dto.response.CategoryDto;
import com.todo.entity.Category;


public class CategoryMapper {

    public static CategoryDto getCategeoryDto(Category category) {
        CategoryDto categoryDto = new CategoryDto();
        categoryDto.setId(category.getId());
        categoryDto.setLabel(category.getLabel());
        return categoryDto;
    }
}
