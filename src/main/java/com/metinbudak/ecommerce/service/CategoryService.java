package com.metinbudak.ecommerce.service;

import com.metinbudak.ecommerce.dto.CategoryDto;
import com.metinbudak.ecommerce.exception.RecordNotFoundException;
import com.metinbudak.ecommerce.repository.CategoryRepository;
import com.metinbudak.ecommerce.repository.domain.Category;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class CategoryService {

    private final CategoryRepository categoryRepository;

    public CategoryService(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    @Transactional
    public Category createCategory(String name) {
        Category category = new Category(name);
        return categoryRepository.save(category);
    }

    public List<Category> getCategories() {
        return categoryRepository.findAll();
    }

    public Category getCategory(long id) {
        return categoryRepository.findById(id)
                .orElseThrow(() -> new RecordNotFoundException("Category not found with id: " + id));
    }

    @Transactional
    public Category updateCategory(long id, CategoryDto categoryDto) {
        Category category = categoryRepository.findById(id)
                .orElseThrow(() -> new RecordNotFoundException("Category not found with id: " + id));

        category.setName(categoryDto.getName());
        return categoryRepository.save(category);
    }

    @Transactional
    public void deleteCategory(long id) {
        if (!categoryRepository.existsById(id)) {
            throw new RecordNotFoundException("Category not found with id: " + id);
        }
        categoryRepository.deleteById(id);
    }

    // Extra methode toevoegen voor het controleren van het bestaan van een categorie
    public boolean categoryExistsById(long id) {
        return categoryRepository.existsById(id);
    }
}
