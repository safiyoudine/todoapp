package com.todo.service.category;
import com.todo.dto.request.CategoryRequest;
import com.todo.dto.response.CategoryDto;
import java.util.List;

public interface CategoryService {

    CategoryDto createCategory(CategoryRequest categoryRequest);
    CategoryDto getCategoryById(Long id);

    List<CategoryDto> getCategories();

}
