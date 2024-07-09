package com.metinbudak.ecommerce.controller;

import com.metinbudak.ecommerce.repository.domain.Category;
import com.metinbudak.ecommerce.service.CategoryService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(SpringExtension.class)
@WebMvcTest(CategoryController.class)
class CategoryControllerTest {

    @Autowired
    protected MockMvc mockMvc;

    @MockBean
    private CategoryService categoryService;

    @Test
    void addCategory() throws Exception {
        Category categoryMens = new Category("men");
        categoryMens.setId(1L);

        when(categoryService.createCategory("men")).thenReturn(categoryMens); // Corrected method name

        mockMvc.perform(post("/categories")
                        .content("{\"name\": \"men\"}")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andExpect(header().string("Location", "http://localhost/categories/1"));
    }

    @Test
    void getCategories() throws Exception {
        Category categoryMen = new Category("men");
        categoryMen.setId(1L);
        Category categoryWomen = new Category("women");
        categoryWomen.setId(2L);
        List<Category> categories = List.of(categoryMen, categoryWomen);

        when(categoryService.getCategories()).thenReturn(categories);

        mockMvc.perform(get("/categories"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].name").value(categoryMen.getName()))
                .andExpect(jsonPath("$[0].id").value(categoryMen.getId()))
                .andExpect(jsonPath("$[1].name").value(categoryWomen.getName()))
                .andExpect(jsonPath("$[1].id").value(categoryWomen.getId()));
    }

    @Test
    void deleteCategory() throws Exception {
        long categoryId = 1L;

        doNothing().when(categoryService).deleteCategory(anyLong());

        mockMvc.perform(delete("/categories/{id}", categoryId))
                .andExpect(status().isNoContent())
                .andExpect(content().string(""));
    }

    @Test
    void getCategory() throws Exception {
        long categoryId = 1L;
        Category categoryMen = new Category("men");
        categoryMen.setId(categoryId);

        when(categoryService.getCategory(categoryId)).thenReturn(categoryMen);

        mockMvc.perform(get("/categories/{id}", categoryId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value(categoryMen.getName()))
                .andExpect(jsonPath("$.id").value(categoryMen.getId()));
    }
}
