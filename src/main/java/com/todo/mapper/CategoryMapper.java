package com.todo.mapper;

import com.todo.dto.request.TaskRequest;
import com.todo.dto.response.CategoryDto;
import com.todo.dto.response.TaskDto;
import com.todo.entity.Category;
import com.todo.entity.Task;


public class CategoryMapper {

    public static CategoryDto getCategeoryDto(Category category) {
        CategoryDto categoryDto = new CategoryDto();
        categoryDto.setId(category.getId());
        categoryDto.setLabel(category.getLabel());
        return categoryDto;
    }
}
