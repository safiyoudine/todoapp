package com.todo.service.category;

import com.todo.dto.request.CategoryRequest;
import com.todo.dto.response.CategoryDto;
import com.todo.entity.Category;
import com.todo.mapper.CategoryMapper;
import com.todo.repository.CategoryRepository;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class CategoryServiceImpl implements CategoryService {

    @Autowired
    private CategoryRepository categoryRepository;

    @PostConstruct
    public void creatDefaultAccount() {
        Optional<Category> category = categoryRepository.findById(1L);
        if (category.isEmpty()) {
            Category category1 = new Category();
            category1.setId(1L);
            category1.setLabel("empty category");
            categoryRepository.save(category1);
        }
    }

    @Override
    public CategoryDto createCategory(CategoryRequest categoryRequest) {
        Optional<Category> category = categoryRepository.findByLabel(categoryRequest.getLabel());
        if (!category.isPresent()) {
            Category category1 = new Category();
            category1.setLabel(categoryRequest.getLabel());
            return CategoryMapper.getCategeoryDto(categoryRepository.save(category1));
        }
        return null;
    }


    @Override
    public List<CategoryDto> getCategories() {
        return categoryRepository.findAll()
                .stream()
                .map(CategoryMapper::getCategeoryDto)
                .collect(Collectors.toList());
    }

    @Override
    public CategoryDto getCategoryById(Long id) {
        Optional<Category> category = categoryRepository.findById(id);
        return category.map(CategoryMapper::getCategeoryDto).orElse(null);
    }
}
