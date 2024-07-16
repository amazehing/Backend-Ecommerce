package com.metinbudak.ecommerce.repository.domain;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "reviews")
public class Review {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private String comment;
    @Min(1) @Max(5)
    private int rating;

    @ManyToOne
    @JoinColumn(name = "product_id")
    private Product product;

    public Review() {
    }

    public Review(Product product, String name, String comment, int rating) {
        if (rating < 1 || rating > 5) {
            throw new IllegalArgumentException("Rating should be in range from 1-5");
        }

        this.product = product;
        this.name = name;
        this.comment = comment;
        this.rating = rating;
    }

}
