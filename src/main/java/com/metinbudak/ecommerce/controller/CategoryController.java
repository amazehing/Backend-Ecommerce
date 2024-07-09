package com.metinbudak.ecommerce.controller;

import com.metinbudak.ecommerce.dto.CategoryDto;
import com.metinbudak.ecommerce.repository.domain.Category;
import com.metinbudak.ecommerce.service.CategoryService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@RestController
public class CategoryController {

    private CategoryService categoryService;

    public CategoryController(CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    @PostMapping("/categories")
    public ResponseEntity<Object> addCategory(@RequestBody CategoryDto categoryDto) {
        Category category = categoryService.createCategory(categoryDto.getName());
        URI location = URI.create("/categories/" + category.getId());
        return ResponseEntity.created(location).build();
    }

    @GetMapping("/categories")
    public ResponseEntity<List<Category>> getCategories() {
        List<Category> categories = categoryService.getCategories();
        return ResponseEntity.ok(categories);
    }

    @DeleteMapping("/categories/{id}")
    public ResponseEntity<Object> deleteCategory(@PathVariable long id) {
        categoryService.deleteCategory(id);
        return ResponseEntity.noContent().build();
    }
}
