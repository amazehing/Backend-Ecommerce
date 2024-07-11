package com.metinbudak.ecommerce.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
public class ProductCreateUpdateDto {

    private String category;
    private long categoryId;
    @NotBlank
    private String name;
    @Min(0)
    private double new_price;
    @Min(0)
    private double old_price;
    private Set<Long> imageIds = new HashSet<>();

}
