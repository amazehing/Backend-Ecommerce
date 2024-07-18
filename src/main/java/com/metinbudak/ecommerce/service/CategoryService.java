package com.metinbudak.ecommerce.service;

import com.metinbudak.ecommerce.dto.CategoryCreateUpdateDto;
import com.metinbudak.ecommerce.dto.CategoryReadDto;
import com.metinbudak.ecommerce.exception.RecordNotFoundException;
import com.metinbudak.ecommerce.repository.CategoryRepository;
import com.metinbudak.ecommerce.repository.domain.Category;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CategoryService {

    private final CategoryRepository categoryRepository;

    @Transactional
    public CategoryReadDto createCategory(String name) {
        Category category = new Category(name);
        category = categoryRepository.save(category);
        return new CategoryReadDto(category.getId(), category.getName());
    }

    public List<CategoryReadDto> getCategories() {
        List<Category> categories = categoryRepository.findAll();
        return categories.stream()
                .map(category -> new CategoryReadDto(category.getId(), category.getName()))
                .toList();
    }

    public CategoryReadDto getCategory(long id) {
        return categoryRepository.findById(id)
                .map(category -> new CategoryReadDto(category.getId(), category.getName()))
                .orElseThrow(() -> new RecordNotFoundException("Category not found with id: " + id));
    }

    @Transactional
    public void updateCategory(long id, CategoryCreateUpdateDto categoryCreateUpdateDto) {
        Category category = categoryRepository.findById(id)
                .orElseThrow(() -> new RecordNotFoundException("Category not found with id: " + id));
        category.setName(categoryCreateUpdateDto.getName());
        categoryRepository.save(category);
    }

    @Transactional
    public void deleteCategory(long id) {
        if (!categoryRepository.existsById(id)) {
            throw new RecordNotFoundException("Category not found with id: " + id);
        }
        categoryRepository.deleteById(id);
    }

}
