package com.metinbudak.ecommerce.service;

import com.metinbudak.ecommerce.dto.ProductDto;
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

    public List<Product> getAllProducts() {
        return productRepository.findAll();
    }

    public List<Product> getProductsForCategory(long categoryId) {
        if (!categoryRepository.existsById(categoryId)) {
            throw new RecordNotFoundException("Category cannot be found");
        }
        return productRepository.findAllByCategoryId(categoryId);
    }

    @Transactional
    public Product addProduct(long categoryId, ProductDto productDto) {
        Category category = categoryRepository.findById(categoryId).orElseThrow(() -> new RecordNotFoundException("Category cannot be found"));

        Set<Image> images = new HashSet<>();
        for (Long imageId : productDto.getImageIds()) {
            Image image = imageRepository.findById(imageId).orElseThrow(() -> new ImageNotFoundException("Image with id %s cannot be found".formatted(imageId)));
            images.add(image);
        }

        Product product = new Product(
                category,
                productDto.getName(),
                productDto.getNew_price(),
                productDto.getOld_price(),
                images
        );

        return productRepository.save(product);
    }

    public Product getProduct(long productId) {
        return productRepository.findById(productId).orElseThrow(() -> new RecordNotFoundException("Product cannot be found"));
    }

    public void updateProduct(long productId, ProductDto productDto) {
        Product product = getProduct(productId);
        product.setName(productDto.getName());
        product.setNew_price(productDto.getNew_price());
        product.setOld_price(productDto.getOld_price());
        productRepository.save(product);
    }

    public void deleteProduct(long id) {
        Product product = productRepository.findById(id).orElseThrow(() -> new RecordNotFoundException("Product cannot be found"));
        imageRepository.deleteAll(product.getImages());
        productRepository.deleteById(id);
    }

}
