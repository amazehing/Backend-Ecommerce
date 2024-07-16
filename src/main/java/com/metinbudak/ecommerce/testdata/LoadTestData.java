package com.metinbudak.ecommerce.testdata;

import com.metinbudak.ecommerce.repository.CategoryRepository;
import com.metinbudak.ecommerce.repository.ImageRepository;
import com.metinbudak.ecommerce.repository.ProductRepository;
import com.metinbudak.ecommerce.repository.UserRepository;
import com.metinbudak.ecommerce.repository.domain.Category;
import com.metinbudak.ecommerce.repository.domain.Image;
import com.metinbudak.ecommerce.repository.domain.Product;
import com.metinbudak.ecommerce.repository.domain.User;
import com.metinbudak.ecommerce.security.JwtUtil;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Component
@RequiredArgsConstructor
@ConditionalOnProperty(value = "load.test-data", havingValue = "true")
public class LoadTestData {

    private final CategoryRepository categoryRepository;
    private final ProductRepository productRepository;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;
    private final UserDetailsService userDetailsService;

    @PostConstruct
    @Transactional
    protected void loadTestData() {
        Category categoryHeren = this.saveCategory(new Category("Heren"));
        Image imageHeren = new Image("/test/placeholder-men-shirt.jpg");
        Product productHeren = this.saveProduct(new Product(categoryHeren, "(Placeholder) T-shirt heren", 11.95, 18.95, imageHeren));

        Category categoryDames = this.saveCategory(new Category("Dames"));
        Image imageDames = new Image("/test/placeholder-women-shirt.png");
        Product productDames = this.saveProduct(new Product(categoryDames, "(Placeholder) T-shirt dames", 16.95, 23.95, imageDames));

        Category categoryKinderen = this.saveCategory(new Category("Kinderen"));
        Image imageKinderen = new Image("/test/placeholder-kids-shirt.jpg");
        Product productKinderen = this.saveProduct(new Product(categoryKinderen, "(Placeholder) T-shirt kinderen", 9.95, 14.95, imageKinderen));

        User admin = userRepository.save(User.adminUser(
                "admin",
                passwordEncoder.encode("admin!password"),
                "metin",
                "budak",
                "metinbudak@hotmail.com"
        ));

        User john = userRepository.save(User.regularUser(
                "johndoe",
                passwordEncoder.encode("johndoe!password"),
                "john",
                "doe",
                "johndoe@hotmail.com"
        ));

        User jane = userRepository.save(User.regularUser(
                "janedoe",
                passwordEncoder.encode("janedoe!password"),
                "jane",
                "doe",
                "jane@hotmail.com"
        ));

        log.info("[token] admin -> {}", jwtUtil.generateToken(userDetailsService.loadUserByUsername("admin")));
        log.info("[token] johndoe -> {}", jwtUtil.generateToken(userDetailsService.loadUserByUsername("johndoe")));
        log.info("[token] janedoe -> {}", jwtUtil.generateToken(userDetailsService.loadUserByUsername("janedoe")));
    }

    private Category saveCategory(Category category) {
        return categoryRepository.findByName(category.getName())
                .orElseGet(() -> categoryRepository.save(category));
    }

    private Product saveProduct(Product product) {
        return productRepository.findByCategoryIdAndName(product.getCategory().getId(), product.getName())
                .orElseGet(() -> productRepository.save(product));
    }

}
