package com.metinbudak.ecommerce.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ProductReadDto {

    private CategoryReadDto category;
    private long id;
    private String name;
    private double new_price;
    private double old_price;
    private Set<ImageReadDto> images = new HashSet<>();


}
