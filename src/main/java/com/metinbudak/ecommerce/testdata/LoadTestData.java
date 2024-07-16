package com.metinbudak.ecommerce.testdata;

import com.metinbudak.ecommerce.repository.CategoryRepository;
import com.metinbudak.ecommerce.repository.ImageRepository;
import com.metinbudak.ecommerce.repository.ProductRepository;
import com.metinbudak.ecommerce.repository.domain.Category;
import com.metinbudak.ecommerce.repository.domain.Image;
import com.metinbudak.ecommerce.repository.domain.Product;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@RequiredArgsConstructor
@ConditionalOnProperty(value = "load.test-data", havingValue = "true")
public class LoadTestData {

    private final CategoryRepository categoryRepository;
    private final ProductRepository productRepository;
    private final ImageRepository imageRepository;

    @PostConstruct
    @Transactional
    protected void loadTestData() {
        Category categoryHeren = this.saveCategory(new Category("Heren"));
        Image imageHeren = this.saveImage(new Image("/test/placeholder-men-shirt.jpg"));
        Product productHeren = this.saveProduct(new Product(categoryHeren, "(Placeholder) T-shirt heren", 11.95, 18.95, imageHeren));

        Category categoryDames = this.saveCategory(new Category("Dames"));
        Image imageDames = this.saveImage(new Image("/test/placeholder-women-shirt.png"));
        Product productDames = this.saveProduct(new Product(categoryDames, "(Placeholder) T-shirt dames", 16.95, 23.95, imageDames));

        Category categoryKinderen = this.saveCategory(new Category("Kinderen"));
        Image imageKinderen = this.saveImage(new Image("/test/placeholder-kids-shirt.jpg"));
        Product productKinderen = this.saveProduct(new Product(categoryKinderen, "(Placeholder) T-shirt kinderen", 9.95, 14.95, imageKinderen));
    }

    private Category saveCategory(Category category) {
        return categoryRepository.findByName(category.getName())
                .orElseGet(() -> categoryRepository.save(category));
    }

    private Image saveImage(Image image) {
        return imageRepository.findByLocation(image.getLocation())
                .orElse(image);
    }

    private Product saveProduct(Product product) {
        return productRepository.findByCategoryIdAndName(product.getCategory().getId(), product.getName())
                .orElseGet(() -> productRepository.save(product));
    }

}
