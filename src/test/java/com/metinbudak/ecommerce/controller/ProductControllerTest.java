package com.metinbudak.ecommerce.controller;

import com.metinbudak.ecommerce.controller.ProductController;
import com.metinbudak.ecommerce.dto.ProductDto;
import com.metinbudak.ecommerce.repository.domain.Category;
import com.metinbudak.ecommerce.repository.domain.Image;
import com.metinbudak.ecommerce.repository.domain.Product;
import com.metinbudak.ecommerce.service.ProductService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;
import java.util.Set;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(SpringExtension.class)
@WebMvcTest(ProductController.class)
class ProductControllerTest {

    @Autowired
    protected MockMvc mockMvc;
    @MockBean
    private ProductService productService;

    @Test
    void getAllProducts() throws Exception {
        Product shirtYellow = createProductMock("t-shirt yellow");
        Product shirtBlue = createProductMock("t-shirt blue");
        List<Product> products = List.of(shirtYellow, shirtBlue);

        when(productService.getAllProducts()).thenReturn(products);

        mockMvc.perform(get("/products"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(shirtYellow.getId()))
                .andExpect(jsonPath("$[0].name").value(shirtYellow.getName()))
                .andExpect(jsonPath("$[0].new_price").value(shirtYellow.getNew_price()))
                .andExpect(jsonPath("$[0].old_price").value(shirtYellow.getOld_price()))
                .andExpect(jsonPath("$[1].id").value(shirtBlue.getId()))
                .andExpect(jsonPath("$[1].name").value(shirtBlue.getName()))
                .andExpect(jsonPath("$[1].new_price").value(shirtBlue.getNew_price()))
                .andExpect(jsonPath("$[1].old_price").value(shirtBlue.getOld_price()));
    }

    @Test
    void getProductsForCategory() throws Exception {
        long categoryId = 1L;
        Product shirtYellow = createProductMock("t-shirt yellow");
        Product shirtBlue = createProductMock("t-shirt blue");
        List<Product> products = List.of(shirtYellow, shirtBlue);

        when(productService.getProductsForCategory(categoryId)).thenReturn(products);

        mockMvc.perform(get("/categories/{categoryId}/products", categoryId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(shirtYellow.getId()))
                .andExpect(jsonPath("$[0].name").value(shirtYellow.getName()))
                .andExpect(jsonPath("$[0].new_price").value(shirtYellow.getNew_price()))
                .andExpect(jsonPath("$[0].old_price").value(shirtYellow.getOld_price()))
                .andExpect(jsonPath("$[1].id").value(shirtBlue.getId()))
                .andExpect(jsonPath("$[1].name").value(shirtBlue.getName()))
                .andExpect(jsonPath("$[1].new_price").value(shirtBlue.getNew_price()))
                .andExpect(jsonPath("$[1].old_price").value(shirtBlue.getOld_price()));
    }

    @Test
    void addProduct() throws Exception {
        long categoryId = 1L;
        Product shirtYellow = createProductMock("t-shirt yellow");

        when(productService.addProduct(eq(categoryId), any(ProductDto.class))).thenReturn(shirtYellow);

        mockMvc.perform(post("/categories/{categoryId}/products", categoryId)
                        .content("""
                                {
                                    "name": "t-shirt yellow",
                                    "new_price": 10.50,
                                    "old_price": 12.00,
                                    "imageIds": [
                                        1,
                                        2
                                    ]
                                }
                                """)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated());
    }

    @Test
    void getProduct() throws Exception {
        long productId = 1L;
        Product shirtYellow = createProductMock("t-shirt yellow");

        when(productService.getProduct(productId)).thenReturn(shirtYellow);

        mockMvc.perform(get("/products/{productId}", productId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(shirtYellow.getId()))
                .andExpect(jsonPath("$.name").value(shirtYellow.getName()))
                .andExpect(jsonPath("$.new_price").value(shirtYellow.getNew_price()))
                .andExpect(jsonPath("$.old_price").value(shirtYellow.getOld_price()));
    }

    @Test
    void updateProduct() throws Exception {
        long productId = 1L;
        doNothing().when(productService).updateProduct(eq(productId), any(ProductDto.class));

        mockMvc.perform(put("/products/{productId}", productId)
                        .content("""
                                {
                                    "name": "t-shirt yellow",
                                    "new_price": 10.50,
                                    "old_price": 12.00,
                                    "imageIds": [
                                        1,
                                        2
                                    ]
                                }
                                """)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());
    }

    @Test
    void deleteProduct() throws Exception {
        long productId = 1L;

        doNothing().when(productService).deleteProduct(productId);

        mockMvc.perform(delete("/products/{id}", productId))
                .andExpect(status().isNoContent())
                .andExpect(content().string(""));
    }

    public Product createProductMock(String productName){
        Category category = new Category("random-category");
        category.setId(1L);
        Image image = new Image("/fake/location/image.png");
        Product product = new Product(category, productName, 20.00, 30.00, Set.of(image));
        product.setId(10L);
        return product;
    }

}