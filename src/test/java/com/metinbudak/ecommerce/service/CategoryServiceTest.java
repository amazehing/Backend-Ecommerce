package com.metinbudak.ecommerce.service;

import com.metinbudak.ecommerce.dto.CategoryCreateUpdateDto;
import com.metinbudak.ecommerce.dto.CategoryReadDto;
import com.metinbudak.ecommerce.exception.RecordNotFoundException;
import com.metinbudak.ecommerce.repository.CategoryRepository;
import com.metinbudak.ecommerce.repository.domain.Category;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@SpringBootTest(properties = "load.test-data=false")
class CategoryServiceTest {

    @MockBean
    private CategoryRepository categoryRepository;

    @Autowired
    private CategoryService categoryService;

    @Test
    void createCategory() {
        // Arrange
        Category expectedCategoryEntity = new Category(1L, "men");

        ArgumentCaptor<Category> categoryToBeSavedCaptor = ArgumentCaptor.forClass(Category.class);
        when(categoryRepository.save(categoryToBeSavedCaptor.capture())).thenReturn(expectedCategoryEntity);

        // Act
        CategoryReadDto categoryReadDto = categoryService.createCategory("men");

        // Assert
        Category savedCategory = categoryToBeSavedCaptor.getValue();
        assertNull(savedCategory.getId());
        assertEquals("men", savedCategory.getName());

        assertEquals(1L, categoryReadDto.getId());
        assertEquals("men", categoryReadDto.getName());
    }

    @Test
    void getCategories() {
        // Arrange
        List<Category> categories = List.of(
                new Category(1L, "men"),
                new Category(2L, "women"),
                new Category(3L, "kids")
        );

        when(categoryRepository.findAll()).thenReturn(categories);

        // Act
        List<CategoryReadDto> categoryReadDtos = categoryService.getCategories();

        // Assert
        assertEquals(3, categoryReadDtos.size());
    }

    @Test
    void getCategory() {
        // Arrange
        Category category = new Category(1L, "men");
        when(categoryRepository.findById(1L)).thenReturn(Optional.of(category));

        // Act
        CategoryReadDto categoryReadDto = categoryService.getCategory(1L);

        // Assert
        assertEquals(category.getId(), categoryReadDto.getId());
        assertEquals(category.getName(), categoryReadDto.getName());
    }

    @Test
    void getCategoryThrowsIfCategoryDoesntExists() {
        // Arrange
        when(categoryRepository.findById(1L)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(RecordNotFoundException.class, () -> categoryService.getCategory(1L));
    }

    @Test
    void updateCategory() {
        // Arrange
        CategoryCreateUpdateDto categoryCreateUpdateDto = new CategoryCreateUpdateDto();
        categoryCreateUpdateDto.setName("men++");

        Category categoryToUpdate = new Category(1L, "men");
        Category expectedCategorySave = new Category(1L, "men++");

        ArgumentCaptor<Category> categorySaveCaptor = ArgumentCaptor.forClass(Category.class);
        when(categoryRepository.findById(1L)).thenReturn(Optional.of(categoryToUpdate));
        when(categoryRepository.save(categorySaveCaptor.capture())).thenReturn(expectedCategorySave);

        // Act
        categoryService.updateCategory(1L, categoryCreateUpdateDto);

        // Assert
        Category actualCategorySave = categorySaveCaptor.getValue();
        assertEquals(1L, actualCategorySave.getId());
        assertEquals(expectedCategorySave.getName(), actualCategorySave.getName());
    }

    @Test
    void updateCategoryThrowsIfCategoryDoesntExists() {
        // Arrange
        CategoryCreateUpdateDto categoryCreateUpdateDto = new CategoryCreateUpdateDto();
        categoryCreateUpdateDto.setName("men++");

        when(categoryRepository.findById(1L)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(RecordNotFoundException.class, () -> categoryService.updateCategory(1L, categoryCreateUpdateDto));
    }

    @Test
    void deleteCategory() {
        // Arrange
        long categoryId = 1L;

        when(categoryRepository.existsById(categoryId)).thenReturn(true);
        doNothing().when(categoryRepository).deleteById(categoryId);

        // Act
        categoryService.deleteCategory(categoryId);

        // Assert
        verify(categoryRepository).existsById(categoryId);
        verify(categoryRepository).deleteById(categoryId);
    }

    @Test
    void deleteCategoryThrowsErrorIfCategoryDoesntExists() {
        // Arrange
        long categoryId = 1L;

        when(categoryRepository.existsById(categoryId)).thenReturn(false);

        // Act & Assert
        assertThrows(RecordNotFoundException.class, () -> categoryService.deleteCategory(categoryId));
    }

}
