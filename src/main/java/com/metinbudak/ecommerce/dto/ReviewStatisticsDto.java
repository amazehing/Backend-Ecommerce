package com.metinbudak.ecommerce.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ReviewStatisticsDto {

    private int totalReviews;
    private double averageRating;

    public ReviewStatisticsDto(int totalReviews, double averageRating) {
        this.totalReviews = totalReviews;
        this.averageRating = averageRating;
    }

}
