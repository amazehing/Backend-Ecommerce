package com.metinbudak.ecommerce.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.metinbudak.ecommerce.EcommerceApplication;
import com.metinbudak.ecommerce.dto.CategoryCreateUpdateDto;
import com.metinbudak.ecommerce.repository.CategoryRepository;
import com.metinbudak.ecommerce.repository.domain.Category;
import com.metinbudak.ecommerce.security.config.MySecurityConfig;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.hamcrest.Matchers.hasSize;
import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest(classes = EcommerceApplication.class, properties = "load.test-data=false")
@ExtendWith(SpringExtension.class)
@AutoConfigureMockMvc
@Transactional
class CategoryControllerIntegrationTest {

    @Autowired
    protected MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private CategoryRepository categoryRepository;

    @Test
    @WithMockUser(roles = MySecurityConfig.ROLE_ADMIN)
    void addCategory() throws Exception {
        CategoryCreateUpdateDto categoryDto = new CategoryCreateUpdateDto();
        categoryDto.setName("men");

        mockMvc.perform(post("/categories")
                        .content(objectMapper.writeValueAsString(categoryDto))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andExpect(header().exists("Location"));

    }

    @Test
    @WithMockUser(roles = MySecurityConfig.ROLE_USER)
    void addCategoryAsNonAdminIsForbidden() throws Exception {
        CategoryCreateUpdateDto categoryDto = new CategoryCreateUpdateDto();
        categoryDto.setName("men");

        mockMvc.perform(post("/categories")
                        .content(objectMapper.writeValueAsString(categoryDto))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isForbidden());
    }

    @Test
    void getCategories() throws Exception {
        Category men = categoryRepository.save(new Category("men"));
        Category women = categoryRepository.save(new Category("women"));
        Category kids = categoryRepository.save(new Category("kids"));

        mockMvc.perform(get("/categories")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[*]", hasSize(3)))
                .andExpect(jsonPath("$[*].id", containsInAnyOrder(
                        men.getId().intValue(),
                        women.getId().intValue(),
                        kids.getId().intValue())))
                .andExpect(jsonPath("$[*].name", containsInAnyOrder(
                        men.getName(),
                        women.getName(),
                        kids.getName())));
    }

    @Test
    @WithMockUser(roles = MySecurityConfig.ROLE_ADMIN)
    void getCategoryById() throws Exception {
        Category men = categoryRepository.save(new Category("men"));
        categoryRepository.save(new Category("women"));

        mockMvc.perform(get("/categories/{id}", men.getId())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(men.getId()))
                .andExpect(jsonPath("$.name").value(men.getName()));
    }

    @Test
    @WithMockUser(roles = MySecurityConfig.ROLE_ADMIN)
    void updateCategory() throws Exception {
        Category men = categoryRepository.save(new Category("men"));

        CategoryCreateUpdateDto categoryUpdateDto = new CategoryCreateUpdateDto();
        categoryUpdateDto.setName("men++");

        mockMvc.perform(put("/categories/{id}",men.getId())
                        .content(objectMapper.writeValueAsString(categoryUpdateDto))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());

        Category updatedCategory = categoryRepository.findById(men.getId()).orElseThrow();

        assertEquals(men.getId(), updatedCategory.getId());
        assertEquals(categoryUpdateDto.getName(), updatedCategory.getName());
    }

    @Test
    @WithMockUser(roles = MySecurityConfig.ROLE_USER)
    void updateCategoryNotAllowedForNonAdmins() throws Exception {
        Category men = categoryRepository.save(new Category("men"));

        CategoryCreateUpdateDto categoryUpdateDto = new CategoryCreateUpdateDto();
        categoryUpdateDto.setName("men++");

        mockMvc.perform(put("/categories/{id}",men.getId())
                        .content(objectMapper.writeValueAsString(categoryUpdateDto))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isForbidden());

        Category actualCategory = categoryRepository.findById(men.getId()).orElseThrow();

        assertEquals(men.getId(), actualCategory.getId());
        assertEquals(men.getName(), actualCategory.getName());
    }

    @Test
    @WithMockUser(roles = MySecurityConfig.ROLE_ADMIN)
    void deleteCategory() throws Exception {
        Category men = categoryRepository.save(new Category("men"));
        Category women = categoryRepository.save(new Category("women"));

        assertEquals(2, categoryRepository.count());

        mockMvc.perform(delete("/categories/{id}", men.getId())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());

        assertFalse(categoryRepository.existsById(men.getId()));
        assertTrue(categoryRepository.existsById(women.getId()));
    }

    @Test
    @WithMockUser(roles = MySecurityConfig.ROLE_USER)
    void deleteCategoryNotAllowedForNonAdmins() throws Exception {
        Category men = categoryRepository.save(new Category("men"));
        categoryRepository.save(new Category("women"));

        assertEquals(2, categoryRepository.count());

        // Use the extracted ID to perform the GET request
        mockMvc.perform(delete("/categories/{id}", men.getId())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isForbidden());

        assertEquals(2, categoryRepository.count());
    }

}
