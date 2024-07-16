package com.metinbudak.ecommerce.controller;

import com.metinbudak.ecommerce.dto.ReviewCreateDto;
import com.metinbudak.ecommerce.dto.ReviewReadDto;
import com.metinbudak.ecommerce.dto.ReviewStatisticsDto;
import com.metinbudak.ecommerce.service.ProductReviewService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;

@RequiredArgsConstructor
@RestController
@Validated
public class ProductReviewController {

    private final ProductReviewService productReviewService;

    @PostMapping("/products/{productId}/reviews")
    public ResponseEntity<Object> addReviewToProduct(@PathVariable long productId, @RequestBody ReviewCreateDto reviewCreateDto) {
        ReviewReadDto review = productReviewService.addReview(productId, reviewCreateDto);
        URI location = ServletUriComponentsBuilder.fromCurrentRequest()
                .replacePath("/reviews/{id}")
                .buildAndExpand(review.getId())
                .toUri();
        return ResponseEntity.created(location).build();
    }

    @GetMapping("/reviews/{reviewId}")
    public ResponseEntity<ReviewReadDto> getReview(@PathVariable long reviewId) {
        ReviewReadDto review = productReviewService.getReview(reviewId);
        return ResponseEntity.ok(review);
    }

    @DeleteMapping("/reviews/{reviewId}")
    public ResponseEntity<Object> deleteProduct(@PathVariable long reviewId) {
        productReviewService.deleteReview(reviewId);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/products/{productId}/reviews")
    public ResponseEntity<List<ReviewReadDto>> getReviewsForProduct(@PathVariable long productId) {
        List<ReviewReadDto> reviewsForProduct = productReviewService.getReviewsForProduct(productId);
        return ResponseEntity.ok(reviewsForProduct);
    }

    @GetMapping("/products/{productId}/reviews/statistics")
    public ResponseEntity<ReviewStatisticsDto> getStatistics(@PathVariable long productId) {
        ReviewStatisticsDto statistics = productReviewService.getStatistics(productId);
        return ResponseEntity.ok(statistics);
    }

}
