package com.metinbudak.ecommerce.controller;

import com.metinbudak.ecommerce.dto.ProductCreateUpdateDto;
import com.metinbudak.ecommerce.dto.ProductReadDto;
import com.metinbudak.ecommerce.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
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

@RequiredArgsConstructor
@RestController
@Validated
public class ProductController {

    private final ProductService productService;

    @GetMapping("/products")
    public ResponseEntity<List<ProductReadDto>> getAllProducts() {
        List<ProductReadDto> categories = productService.getAllProducts();
        return ResponseEntity.ok(categories);
    }

    @GetMapping("/categories/{categoryId}/products")
    public ResponseEntity<List<ProductReadDto>> getProducts(@PathVariable long categoryId) {
        List<ProductReadDto> categories = productService.getProductsForCategory(categoryId);
        return ResponseEntity.ok(categories);
    }

    @PostMapping("/categories/{categoryId}/products")
    public ResponseEntity<Object> addProduct(@PathVariable long categoryId, @RequestBody ProductCreateUpdateDto productCreateUpdateDto) {
        ProductReadDto product = productService.addProduct(categoryId, productCreateUpdateDto);
        URI location = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}")
                .buildAndExpand(product.getId()).toUri();
        return ResponseEntity.created(location).build();
    }

    @GetMapping("/products/{productId}")
    public ResponseEntity<ProductReadDto> getProduct(@PathVariable long productId) {
        ProductReadDto product = productService.getProduct(productId);
        return ResponseEntity.ok(product);
    }

    @PutMapping("/products/{productId}")
    public ResponseEntity<Object> updateProduct(@PathVariable long productId, @RequestBody ProductCreateUpdateDto productCreateUpdateDto) {
        productService.updateProduct(productId, productCreateUpdateDto);
        return ResponseEntity.noContent().build();
    }


    @DeleteMapping("/products/{id}")
    public ResponseEntity<Object> deleteProduct(@PathVariable long id) {
        productService.deleteProduct(id);
        return ResponseEntity.noContent().build();
    }

}
