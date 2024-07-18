package com.metinbudak.ecommerce.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.metinbudak.ecommerce.EcommerceApplication;
import com.metinbudak.ecommerce.dto.ProductCreateUpdateDto;
import com.metinbudak.ecommerce.repository.CategoryRepository;
import com.metinbudak.ecommerce.repository.ProductRepository;
import com.metinbudak.ecommerce.repository.domain.Category;
import com.metinbudak.ecommerce.repository.domain.Product;
import com.metinbudak.ecommerce.security.config.MySecurityConfig;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import java.util.Set;

import static org.hamcrest.Matchers.hasSize;
import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest(classes = EcommerceApplication.class, properties = "load.test-data=false")
@ExtendWith(SpringExtension.class)
@AutoConfigureMockMvc
@Transactional
class ProductControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private CategoryRepository categoryRepository;

    @Test
    @WithMockUser(roles = MySecurityConfig.ROLE_ADMIN)
    void addProduct() throws Exception {
        Category category = categoryRepository.save(new Category("clothing"));

        ProductCreateUpdateDto productDto = new ProductCreateUpdateDto();
        productDto.setName("t-shirt yellow");
        productDto.setNew_price(10.50);
        productDto.setOld_price(12.00);

        mockMvc.perform(post("/categories/{categoryId}/products", category.getId())
                        .content(objectMapper.writeValueAsString(productDto))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andExpect(header().exists("Location"));
    }

    @Test
    @WithMockUser(roles = MySecurityConfig.ROLE_USER)
    void addProductAsNonAdminIsForbidden() throws Exception {
        Category category = categoryRepository.save(new Category("clothing"));

        ProductCreateUpdateDto productDto = new ProductCreateUpdateDto();
        productDto.setName("t-shirt yellow");
        productDto.setNew_price(10.50);
        productDto.setOld_price(12.00);

        mockMvc.perform(post("/categories/{categoryId}/products", category.getId())
                        .content(objectMapper.writeValueAsString(productDto))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isForbidden());
    }

    @Test
    void getAllProducts() throws Exception {
        Category category = categoryRepository.save(new Category("clothing"));
        Product product1 = productRepository.save(new Product(category, "t-shirt yellow", 10.50, 12.00, Set.of()));
        Product product2 = productRepository.save(new Product(category, "t-shirt blue", 12.50, 14.00, Set.of()));

        mockMvc.perform(get("/products"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[*]", hasSize(2)))
                .andExpect(jsonPath("$[0].id").value(product1.getId()))
                .andExpect(jsonPath("$[0].name").value(product1.getName()))
                .andExpect(jsonPath("$[0].new_price").value(product1.getNew_price()))
                .andExpect(jsonPath("$[0].old_price").value(product1.getOld_price()))
                .andExpect(jsonPath("$[1].id").value(product2.getId()))
                .andExpect(jsonPath("$[1].name").value(product2.getName()))
                .andExpect(jsonPath("$[1].new_price").value(product2.getNew_price()))
                .andExpect(jsonPath("$[1].old_price").value(product2.getOld_price()));
    }

    @Test
    void getProductsForCategory() throws Exception {
        Category category = categoryRepository.save(new Category("clothing"));
        Product product1 = productRepository.save(new Product(category, "t-shirt yellow", 10.50, 12.00, Set.of()));
        Product product2 = productRepository.save(new Product(category, "t-shirt blue", 12.50, 14.00, Set.of()));

        Category shoes = categoryRepository.save(new Category("shoes"));
        Product nikeSneakers = productRepository.save(new Product(shoes, "nike sneakers", 101.50, 112.00, Set.of()));

        mockMvc.perform(get("/categories/{id}/products", category.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[*]", hasSize(2)))
                .andExpect(jsonPath("$[0].id").value(product1.getId()))
                .andExpect(jsonPath("$[0].name").value(product1.getName()))
                .andExpect(jsonPath("$[0].new_price").value(product1.getNew_price()))
                .andExpect(jsonPath("$[0].old_price").value(product1.getOld_price()))
                .andExpect(jsonPath("$[1].id").value(product2.getId()))
                .andExpect(jsonPath("$[1].name").value(product2.getName()))
                .andExpect(jsonPath("$[1].new_price").value(product2.getNew_price()))
                .andExpect(jsonPath("$[1].old_price").value(product2.getOld_price()));
    }

    @Test
    void getProductById() throws Exception {
        Category category = categoryRepository.save(new Category("clothing"));
        Product product = productRepository.save(new Product(category, "t-shirt yellow", 10.50, 12.00, Set.of()));

        mockMvc.perform(get("/products/{productId}", product.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(product.getId()))
                .andExpect(jsonPath("$.name").value(product.getName()))
                .andExpect(jsonPath("$.new_price").value(product.getNew_price()))
                .andExpect(jsonPath("$.old_price").value(product.getOld_price()));
    }

    @Test
    @WithMockUser(roles = MySecurityConfig.ROLE_ADMIN)
    void updateProduct() throws Exception {
        Category category = categoryRepository.save(new Category("clothing"));
        Product product = productRepository.save(new Product(category, "t-shirt yellow", 10.50, 12.00, Set.of()));

        ProductCreateUpdateDto productUpdateDto = new ProductCreateUpdateDto();
        productUpdateDto.setName("updated t-shirt");
        productUpdateDto.setNew_price(15.00);
        productUpdateDto.setOld_price(18.00);
        productUpdateDto.setImageIds(Set.of(1L, 2L));

        mockMvc.perform(put("/products/{productId}", product.getId())
                        .content(objectMapper.writeValueAsString(productUpdateDto))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());

        Product updatedProduct = productRepository.findById(product.getId()).orElseThrow();

        assertEquals(product.getId(), updatedProduct.getId());
        assertEquals(productUpdateDto.getName(), updatedProduct.getName());
        assertEquals(productUpdateDto.getNew_price(), updatedProduct.getNew_price());
        assertEquals(productUpdateDto.getOld_price(), updatedProduct.getOld_price());
    }

    @Test
    @WithMockUser(roles = MySecurityConfig.ROLE_USER)
    void updateProductNotAllowedForNonAdmins() throws Exception {
        Category category = categoryRepository.save(new Category("clothing"));
        Product product = productRepository.save(new Product(category, "t-shirt yellow", 10.50, 12.00, Set.of()));

        ProductCreateUpdateDto productUpdateDto = new ProductCreateUpdateDto();
        productUpdateDto.setName("updated t-shirt");
        productUpdateDto.setNew_price(15.00);
        productUpdateDto.setOld_price(18.00);
        productUpdateDto.setImageIds(Set.of(1L, 2L));

        mockMvc.perform(put("/products/{productId}", product.getId())
                        .content(objectMapper.writeValueAsString(productUpdateDto))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isForbidden());

        Product actualProduct = productRepository.findById(product.getId()).orElseThrow();

        assertEquals(product.getId(), actualProduct.getId());
        assertEquals(product.getName(), actualProduct.getName());
        assertEquals(product.getNew_price(), actualProduct.getNew_price());
        assertEquals(product.getOld_price(), actualProduct.getOld_price());
    }

    @Test
    @WithMockUser(roles = MySecurityConfig.ROLE_ADMIN)
    void deleteProduct() throws Exception {
        Category category = categoryRepository.save(new Category("clothing"));
        Product product1 = productRepository.save(new Product(category, "t-shirt yellow", 10.50, 12.00, Set.of()));
        Product product2 = productRepository.save(new Product(category, "t-shirt blue", 12.50, 14.00, Set.of()));

        mockMvc.perform(delete("/products/{id}", product1.getId()))
                .andExpect(status().isNoContent());

        assertFalse(productRepository.existsById(product1.getId()));
        assertTrue(productRepository.existsById(product2.getId()));
    }

    @Test
    @WithMockUser(roles = MySecurityConfig.ROLE_USER)
    void deleteProductNotAllowedForNonAdmins() throws Exception {
        Category category = categoryRepository.save(new Category("clothing"));
        Product product1 = productRepository.save(new Product(category, "t-shirt yellow", 10.50, 12.00, Set.of()));
        Product product2 = productRepository.save(new Product(category, "t-shirt blue", 12.50, 14.00, Set.of()));

        mockMvc.perform(delete("/products/{id}", product1.getId()))
                .andExpect(status().isForbidden());

        assertTrue(productRepository.existsById(product1.getId()));
        assertTrue(productRepository.existsById(product2.getId()));
    }

}