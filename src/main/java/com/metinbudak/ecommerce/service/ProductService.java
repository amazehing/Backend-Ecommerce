package com.metinbudak.ecommerce.service;

import com.metinbudak.ecommerce.dto.CategoryReadDto;
import com.metinbudak.ecommerce.dto.ImageReadDto;
import com.metinbudak.ecommerce.dto.ProductCreateUpdateDto;
import com.metinbudak.ecommerce.dto.ProductReadDto;
import com.metinbudak.ecommerce.exception.ImageNotFoundException;
import com.metinbudak.ecommerce.exception.RecordNotFoundException;
import com.metinbudak.ecommerce.repository.CategoryRepository;
import com.metinbudak.ecommerce.repository.ImageRepository;
import com.metinbudak.ecommerce.repository.ProductRepository;
import com.metinbudak.ecommerce.repository.domain.Category;
import com.metinbudak.ecommerce.repository.domain.Image;
import com.metinbudak.ecommerce.repository.domain.Product;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class ProductService {

    private CategoryRepository categoryRepository;
    private ProductRepository productRepository;
    private ImageRepository imageRepository;

    public ProductService(CategoryRepository categoryRepository, ProductRepository productRepository, ImageRepository imageRepository) {
        this.categoryRepository = categoryRepository;
        this.productRepository = productRepository;
        this.imageRepository = imageRepository;
    }

    public List<ProductReadDto> getAllProducts() {
        List<Product> products = productRepository.findAll();
        return products.stream()
                .map(it -> ProductReadDto.builder().build()).toList();
    }

    public List<ProductReadDto> getProductsForCategory(long categoryId) {
        if (!categoryRepository.existsById(categoryId)) {
            throw new RecordNotFoundException("Category cannot be found");
        }
        List<Product> products = productRepository.findAllByCategoryId(categoryId);
        return products.stream().map(this::mapProduct).toList();
    }

    @Transactional
    public ProductReadDto addProduct(long categoryId, ProductCreateUpdateDto productCreateUpdateDto) {
        Category category = categoryRepository.findById(categoryId).orElseThrow(() -> new RecordNotFoundException("Category cannot be found"));

        Set<Image> images = new HashSet<>();
        for (Long imageId : productCreateUpdateDto.getImageIds()) {
            Image image = imageRepository.findById(imageId).orElseThrow(() -> new ImageNotFoundException("Image with id %s cannot be found".formatted(imageId)));
            images.add(image);
        }

        Product product = new Product(
                category,
                productCreateUpdateDto.getName(),
                productCreateUpdateDto.getNew_price(),
                productCreateUpdateDto.getOld_price(),
                images
        );

        product = productRepository.save(product);
        return this.mapProduct(product);
    }

    public ProductReadDto getProduct(long productId) {
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new RecordNotFoundException("Product cannot be found"));
        return this.mapProduct(product);
    }

    private ProductReadDto mapProduct(Product product) {
        CategoryReadDto category = new CategoryReadDto(product.getCategory().getId(), product.getCategory().getName());
        Set<ImageReadDto> images = product.getImages().stream()
                .map(image -> new ImageReadDto(image.getId(), image.getLocation()))
                .collect(Collectors.toSet());
        return ProductReadDto.builder()
                .id(product.getId())
                .name(product.getName())
                .old_price(product.getOld_price())
                .new_price(product.getNew_price())
                .category(category)
                .images(images)
                .build();
    }

    public void updateProduct(long productId, ProductCreateUpdateDto productCreateUpdateDto) {
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new RecordNotFoundException("Product cannot be found"));
        product.setName(productCreateUpdateDto.getName());
        product.setNew_price(productCreateUpdateDto.getNew_price());
        product.setOld_price(productCreateUpdateDto.getOld_price());
        productRepository.save(product);
    }

    public void deleteProduct(long id) {
        Product product = productRepository.findById(id).orElseThrow(() -> new RecordNotFoundException("Product cannot be found"));
        imageRepository.deleteAll(product.getImages());
        productRepository.deleteById(id);
    }

}
