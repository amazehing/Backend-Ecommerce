package com.metinbudak.ecommerce.service;

import com.metinbudak.ecommerce.dto.ProductDto;
import com.metinbudak.ecommerce.repository.CategoryRepository;
import com.metinbudak.ecommerce.repository.ImageRepository;
import com.metinbudak.ecommerce.repository.ProductRepository;
import com.metinbudak.ecommerce.repository.domain.Category;
import com.metinbudak.ecommerce.repository.domain.Image;
import com.metinbudak.ecommerce.repository.domain.Product;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
class ProductServiceTest {

    @Autowired
    private ProductService productService;
    @Autowired
    private ProductRepository productRepository;
    @Autowired
    private CategoryRepository categoryRepository;
    @Autowired
    private ImageRepository imageRepository;

    @Test
    void getAllProducts() {
        Product tshirtBlue = saveProductInDb("t-shirt blue");
        Product tshirtGreen = saveProductInDb("t-shirt green");

        List<Product> allProducts = productService.getAllProducts();

        assertEquals(2, allProducts.size());
        assertTrue(allProducts.contains(tshirtBlue));
        assertTrue(allProducts.contains(tshirtGreen));
    }

    @Test
    void getProductsForCategory() {
        Product tshirtBlue = saveProductInDb("t-shirt blue");
        Product tshirtGreen = saveProductInDb("t-shirt green");

        List<Product> productsForCategory = productService.getProductsForCategory(tshirtBlue.getCategory().getId());

        assertEquals(1, productsForCategory.size());
        assertTrue(productsForCategory.contains(tshirtBlue));
        assertEquals(2, productRepository.count());
    }

    @Test
    void addProduct() {
        Category category = new Category("random-category");
        category = categoryRepository.save(category);

        Image image = new Image("/fake/location/image.png");
        image = imageRepository.save(image);

        ProductDto productDto = new ProductDto();
        productDto.setName("tshirt blue");
        productDto.setImageIds(Set.of(image.getId()));
        productDto.setNew_price(20.00);
        productDto.setOld_price(30.00);

        Product product = productService.addProduct(category.getId(), productDto);

        assertNotNull(product.getId());
        assertEquals(productDto.getName(), product.getName());
        assertEquals(productDto.getImageIds().size(), product.getImages().size());
        assertEquals(productDto.getNew_price(), product.getNew_price());
        assertEquals(productDto.getOld_price(), product.getOld_price());
    }

    @Test
    void getProduct() {
        Product tshirtBlue = saveProductInDb("t-shirt blue");

        Product tshirtBlueDb = productService.getProduct(tshirtBlue.getId());

        assertEquals(tshirtBlue, tshirtBlueDb);
    }

    @Test
    void deleteProduct() {
        Product tshirtBlue = saveProductInDb("t-shirt blue");
        Product tshirtGreen = saveProductInDb("t-shirt green");

        assertEquals(2, productRepository.count());

        productService.deleteProduct(tshirtBlue.getId());

        assertFalse(productRepository.existsById(tshirtBlue.getId()));
        assertTrue(productRepository.existsById(tshirtGreen.getId()));
    }

    Product saveProductInDb(String productName) {
        Category category = new Category("random-category");
        category = categoryRepository.save(category);

        Image image = new Image("/fake/location/image.png");
        image = imageRepository.save(image);

        Product product = new Product(category, productName, 20.00, 30.00, Set.of(image));
        return productRepository.save(product);
    }

}