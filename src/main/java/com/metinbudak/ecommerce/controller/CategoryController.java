package com.metinbudak.ecommerce.controller;

import com.metinbudak.ecommerce.dto.CategoryCreateUpdateDto;
import com.metinbudak.ecommerce.dto.CategoryReadDto;
import com.metinbudak.ecommerce.service.CategoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;

@RequiredArgsConstructor
@RestController
@Validated
public class CategoryController {

    private final CategoryService categoryService;

    @PostMapping("/categories")
    public ResponseEntity<Object> addCategory(@RequestBody CategoryCreateUpdateDto categoryCreateUpdateDto) {
        CategoryReadDto category = categoryService.createCategory(categoryCreateUpdateDto.getName());
        // Location header from: https://stackoverflow.com/a/72215119/4655324
        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(category.getId())
                .toUri();
        return ResponseEntity.created(location).build();
    }

    @GetMapping("/categories")
    public ResponseEntity<List<CategoryReadDto>> getCategories() {
        List<CategoryReadDto> categories = categoryService.getCategories();
        return ResponseEntity.ok(categories);
    }

    @GetMapping("/categories/{id}")
    public ResponseEntity<CategoryReadDto> getCategoryById(@PathVariable long id) {
        CategoryReadDto category = categoryService.getCategory(id);
        return ResponseEntity.ok(category);
    }

    @PutMapping("/categories/{id}")
    public ResponseEntity<Void> updateCategory(@PathVariable long id, @RequestBody CategoryCreateUpdateDto categoryCreateUpdateDto) {
        categoryService.updateCategory(id, categoryCreateUpdateDto);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/categories/{id}")
    public ResponseEntity<Void> deleteCategory(@PathVariable long id) {
        categoryService.deleteCategory(id);
        return ResponseEntity.noContent().build();
    }

}
