package com.metinbudak.ecommerce.service;

import com.metinbudak.ecommerce.dto.ReviewCreateDto;
import com.metinbudak.ecommerce.dto.ReviewReadDto;
import com.metinbudak.ecommerce.dto.ReviewStatisticsDto;
import com.metinbudak.ecommerce.exception.RecordNotFoundException;
import com.metinbudak.ecommerce.repository.ProductRepository;
import com.metinbudak.ecommerce.repository.ReviewRepository;
import com.metinbudak.ecommerce.repository.domain.Product;
import com.metinbudak.ecommerce.repository.domain.Review;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ProductReviewService {

    private final ProductRepository productRepository;
    private final ReviewRepository reviewRepository;

    public ReviewReadDto addReview(long productId, ReviewCreateDto reviewCreateDto) {
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new RecordNotFoundException("Product cannot be found"));
        Review review = reviewRepository.save(new Review(product, reviewCreateDto.getName(), reviewCreateDto.getComment(), reviewCreateDto.getRating()));
        return mapToDto(review);
    }

    public List<ReviewReadDto> getReviewsForProduct(long productId) {
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new RecordNotFoundException("Product cannot be found"));
        return reviewRepository.findAllByProductId(product.getId()).stream().map(this::mapToDto).toList();
    }

    public ReviewReadDto getReview(long reviewId) {
        return reviewRepository.findById(reviewId)
                .map(this::mapToDto)
                .orElseThrow(() -> new RecordNotFoundException("Review cannot be found"));
    }

    protected ReviewReadDto mapToDto(Review review) {
        return ReviewReadDto.builder()
                .id(review.getId())
                .name(review.getName())
                .comment(review.getComment())
                .rating(review.getRating())
                .build();
    }

    public void deleteReview(long reviewId) {
        Review review = reviewRepository.findById(reviewId)
                .orElseThrow(() -> new RecordNotFoundException("Review cannot be found"));
        reviewRepository.deleteById(review.getId());
    }

    public ReviewStatisticsDto getStatistics(long productId) {
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new RecordNotFoundException("Product cannot be found"));
        int totalReviews = reviewRepository.countByProductId(product.getId());
        double averageRating = reviewRepository.averageRatingByProductId(productId);
        return new ReviewStatisticsDto(totalReviews, averageRating);
    }

}
