package com.metinbudak.ecommerce.service;

import com.metinbudak.ecommerce.dto.ReviewCreateDto;
import com.metinbudak.ecommerce.dto.ReviewReadDto;
import com.metinbudak.ecommerce.dto.ReviewStatisticsDto;
import com.metinbudak.ecommerce.exception.RecordNotFoundException;
import com.metinbudak.ecommerce.repository.ProductRepository;
import com.metinbudak.ecommerce.repository.ReviewRepository;
import com.metinbudak.ecommerce.repository.domain.Category;
import com.metinbudak.ecommerce.repository.domain.Image;
import com.metinbudak.ecommerce.repository.domain.Product;
import com.metinbudak.ecommerce.repository.domain.Review;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.util.List;
import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@SpringBootTest(properties = "load.test-data=false")
class ProductReviewServiceTest {

    @MockBean
    private ProductRepository productRepository;

    @MockBean
    private ReviewRepository reviewRepository;

    @Autowired
    private ProductReviewService productReviewService;

    @Test
    void addReview() {
        // Arrange
        Product product = this.createProduct();

        ReviewCreateDto reviewCreateDto = new ReviewCreateDto();
        reviewCreateDto.setName("John Doe");
        reviewCreateDto.setComment("Nice t-shirt, love it!");
        reviewCreateDto.setRating(5);

        Review expectedReview = this.createReview(product);

        ArgumentCaptor<Review> reviewArgumentCaptor = ArgumentCaptor.forClass(Review.class);

        when(productRepository.findById(product.getId())).thenReturn(Optional.of(product));
        when(reviewRepository.save(reviewArgumentCaptor.capture())).thenReturn(expectedReview);

        // Act
        ReviewReadDto reviewReadDto = productReviewService.addReview(product.getId(), reviewCreateDto);

        // Assert
        assertEquals(expectedReview.getId(), reviewReadDto.getId());
        assertEquals(expectedReview.getName(), reviewReadDto.getName());
        assertEquals(expectedReview.getComment(), reviewReadDto.getComment());
        assertEquals(expectedReview.getRating(), reviewReadDto.getRating());
    }

    @Test
    void addReviewThrowsExceptionIfProductDoesntExists() {
        // Arrange
        long productId = 1L;

        ReviewCreateDto reviewCreateDto = new ReviewCreateDto();
        reviewCreateDto.setName("John Doe");
        reviewCreateDto.setComment("Nice t-shirt, love it!");
        reviewCreateDto.setRating(5);

        when(productRepository.findById(productId)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(RecordNotFoundException.class, () -> productReviewService.addReview(productId, reviewCreateDto));
    }

    @Test
    void getReviewsForProduct() {
        // Arrange
        Product product = createProduct();
        List<Review> reviews = List.of(
                createReview(product),
                createReview(product),
                createReview(product)
        );

        when(productRepository.findById(product.getId())).thenReturn(Optional.of(product));
        when(reviewRepository.findAllByProductId(product.getId())).thenReturn(reviews);

        // Act
        List<ReviewReadDto> reviewsForProduct = productReviewService.getReviewsForProduct(product.getId());

        // Assert
        assertEquals(reviews.size(), reviewsForProduct.size());
    }

    @Test
    void getReviewsForProductMapCorrectlyToDto() {
        // Arrange
        Product product = createProduct();
        Review review = createReview(product);

        when(productRepository.findById(product.getId())).thenReturn(Optional.of(product));
        when(reviewRepository.findAllByProductId(product.getId())).thenReturn(List.of(review));

        // Act
        List<ReviewReadDto> reviewsForProduct = productReviewService.getReviewsForProduct(product.getId());

        // Assert
        assertEquals(1, reviewsForProduct.size());

        ReviewReadDto reviewReadDto = reviewsForProduct.get(0);
        assertEquals(review.getId(), reviewReadDto.getId());
        assertEquals(review.getName(), reviewReadDto.getName());
        assertEquals(review.getComment(), reviewReadDto.getComment());
        assertEquals(review.getRating(), reviewReadDto.getRating());
    }

    @Test
    void getReviewsForProductThrowsIfProductDoesntExists() {
        // Arrange
        long productId = 1L;

        when(productRepository.findById(productId)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(RecordNotFoundException.class, () -> productReviewService.getReviewsForProduct(productId));
    }

    @Test
    void getReview() {
        // Arrange
        Review review = this.createReview(this.createProduct());

        when(reviewRepository.findById(review.getId())).thenReturn(Optional.of(review));

        // Act
        ReviewReadDto reviewReadDto = productReviewService.getReview(review.getId());

        // Assert
        assertEquals(review.getId(), reviewReadDto.getId());
        assertEquals(review.getName(), reviewReadDto.getName());
        assertEquals(review.getComment(), reviewReadDto.getComment());
        assertEquals(review.getRating(), reviewReadDto.getRating());
    }

    @Test
    void getReviewThrowsIfNotFound() {
        // Arrange
        when(reviewRepository.findById(1L)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(RecordNotFoundException.class, () -> productReviewService.getReview(1L));
    }

    @Test
    void mapToDto() {
        // Arrange
        Review review = createReview(createProduct());

        // Act
        ReviewReadDto reviewReadDto = productReviewService.mapToDto(review);

        // Assert
        assertEquals(review.getId(), reviewReadDto.getId());
        assertEquals(review.getName(), reviewReadDto.getName());
        assertEquals(review.getComment(), reviewReadDto.getComment());
        assertEquals(review.getRating(), reviewReadDto.getRating());
    }

    @Test
    void deleteReview() {
        // Arrange
        Review review = createReview(createProduct());

        when(reviewRepository.findById(review.getId())).thenReturn(Optional.of(review));
        doNothing().when(reviewRepository).deleteById(review.getId());

        // Act
        productReviewService.deleteReview(review.getId());

        // Assert
        verify(reviewRepository).deleteById(review.getId());
    }

    @Test
    void deleteReviewThrowsIfReviewNotFound() {
        // Arrange
        long reviewId = 1L;

        when(reviewRepository.findById(reviewId)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(RecordNotFoundException.class, () -> productReviewService.deleteReview(reviewId));
    }

    @Test
    void getStatistics() {
        // Arrange
        Product product = this.createProduct();
        int totalReviews = 250;
        double averageRating = 4.60;

        when(productRepository.findById(product.getId())).thenReturn(Optional.of(product));
        when(reviewRepository.countByProductId(product.getId())).thenReturn(totalReviews);
        when(reviewRepository.averageRatingByProductId(product.getId())).thenReturn(averageRating);

        // Act
        ReviewStatisticsDto statistics = productReviewService.getStatistics(product.getId());

        // Assert
        assertEquals(totalReviews, statistics.getTotalReviews());
        assertEquals(averageRating, statistics.getAverageRating());
    }

    @Test
    void getStatisticsThrowsIfProductNotFound() {
        // Arrange
        long productId = 1L;

        when(productRepository.findById(productId)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(RecordNotFoundException.class, () -> productReviewService.getStatistics(productId));
    }

    public Product createProduct() {
        Category category = new Category(1L, "men");
        Image image = new Image("/fake/location/shirt.png");

        Product product = new Product(category, "t-shirt", 12.00, 14.00, Set.of(image));
        product.setId(100L);
        return product;
    }

    public Review createReview(Product product) {
        Review review = new Review(product, "John Doen", "Nice t-shirt", 5);
        review.setId(200L);
        return review;
    }

}