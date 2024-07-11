package com.metinbudak.ecommerce.service;

import com.metinbudak.ecommerce.dto.ProductCreateUpdateDto;
import com.metinbudak.ecommerce.dto.ProductReadDto;
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

        List<ProductReadDto> allProducts = productService.getAllProducts();

        assertEquals(2, allProducts.size());
        assertTrue(allProducts.contains(tshirtBlue));
        assertTrue(allProducts.contains(tshirtGreen));
    }

    @Test
    void getProductsForCategory() {
        Product tshirtBlue = saveProductInDb("t-shirt blue");
        Product tshirtGreen = saveProductInDb("t-shirt green");

        List<ProductReadDto> productsForCategory = productService.getProductsForCategory(tshirtBlue.getCategory().getId());

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

        ProductCreateUpdateDto productCreateUpdateDto = new ProductCreateUpdateDto();
        productCreateUpdateDto.setName("tshirt blue");
        productCreateUpdateDto.setImageIds(Set.of(image.getId()));
        productCreateUpdateDto.setNew_price(20.00);
        productCreateUpdateDto.setOld_price(30.00);

        ProductReadDto product = productService.addProduct(category.getId(), productCreateUpdateDto);

        assertNotNull(product.getId());
        assertEquals(productCreateUpdateDto.getName(), product.getName());
        assertEquals(productCreateUpdateDto.getImageIds().size(), product.getImages().size());
        assertEquals(productCreateUpdateDto.getNew_price(), product.getNew_price());
        assertEquals(productCreateUpdateDto.getOld_price(), product.getOld_price());
    }

    @Test
    void getProduct() {
        Product tshirtBlue = saveProductInDb("t-shirt blue");

        ProductReadDto tshirtBlueDb = productService.getProduct(tshirtBlue.getId());

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