package com.metinbudak.ecommerce.dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ReviewCreateDto {

    @NotBlank
    private String name;

    @NotBlank
    private String comment;

    @Min(1) @Max(5)
    private int rating;

}
