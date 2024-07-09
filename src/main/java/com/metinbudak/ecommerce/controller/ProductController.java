package com.metinbudak.ecommerce.controller;

import com.metinbudak.ecommerce.dto.ProductDto;
import com.metinbudak.ecommerce.repository.domain.Product;
import com.metinbudak.ecommerce.service.ProductService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;

@RestController
public class ProductController {

    private ProductService productService;

    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    @GetMapping("/products")
    public ResponseEntity<List<Product>> getAllProducts() {
        List<Product> categories = productService.getAllProducts();
        return ResponseEntity.ok(categories);
    }

    @GetMapping("/categories/{categoryId}/products")
    public ResponseEntity<List<Product>> getProducts(@PathVariable long categoryId) {
        List<Product> categories = productService.getProductsForCategory(categoryId);
        return ResponseEntity.ok(categories);
    }

    @PostMapping("/categories/{categoryId}/products")
    public ResponseEntity<Object> addProduct(@PathVariable long categoryId, @RequestBody ProductDto productDto) {
        Product product = productService.addProduct(categoryId, productDto);
        URI location = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}")
                .buildAndExpand(product.getId()).toUri();
        return ResponseEntity.created(location).build();
    }

    @GetMapping("/products/{productId}")
    public ResponseEntity<Product> getProduct(@PathVariable long productId) {
        Product product = productService.getProduct(productId);
        return ResponseEntity.ok(product);
    }

    @PutMapping("/products/{productId}")
    public ResponseEntity<Object> updateProduct(@PathVariable long productId, @RequestBody ProductDto productDto) {
        productService.updateProduct(productId, productDto);
        return ResponseEntity.noContent().build();
    }


    @DeleteMapping("/products/{id}")
    public ResponseEntity<Object> deleteProduct(@PathVariable long id) {
        productService.deleteProduct(id);
        return ResponseEntity.noContent().build();
    }

}
