package com.metinbudak.ecommerce.service;

import com.metinbudak.ecommerce.repository.CategoryRepository;
import com.metinbudak.ecommerce.repository.domain.Category;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
class CategoryServiceTest {

    @Autowired
    private CategoryRepository categoryRepository;
    @Autowired
    private CategoryService categoryService;

    @Test
    void createCategory() {
        Category category = categoryService.createCategory("men");

        assertNotNull(category.getId());
        assertEquals("men", category.getName());
        assertEquals(1, categoryRepository.count());
    }

    @Test
    void getCategories() {
        categoryService.createCategory("men");
        categoryService.createCategory("women");
        categoryService.createCategory("kids");

        List<Category> categories = categoryService.getCategories();

        assertEquals(3, categories.size());
    }

    @Test
    void deleteCategory() {
        Category category = categoryService.createCategory("men");
        categoryService.createCategory("women");

        assertEquals(2, categoryRepository.count());

        categoryService.deleteCategory(category.getId());

        assertEquals(1, categoryRepository.count());
        assertFalse(categoryRepository.existsById(category.getId()));
    }
}
