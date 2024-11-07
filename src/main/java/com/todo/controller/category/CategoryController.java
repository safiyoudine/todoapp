package com.todo.controller.category;

import com.todo.dto.request.CategoryRequest;
import com.todo.dto.response.CategoryDto;
import com.todo.service.category.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
public class CategoryController {

    @Autowired
    private CategoryService categoryService;

    @GetMapping("/categories")
    public ResponseEntity<?> getTasksForConnectedUser() {
        List<CategoryDto> categories = categoryService.getCategories();
        return new ResponseEntity<>(categories, HttpStatus.OK);
    }

    @PostMapping("/category")
    public ResponseEntity<?> createTask(@RequestBody CategoryRequest categoryRequest) {
        CategoryDto categoryDto = categoryService.createCategory(categoryRequest);
        if (categoryDto == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
        return ResponseEntity.status(HttpStatus.CREATED).body(categoryDto);
    }

}
