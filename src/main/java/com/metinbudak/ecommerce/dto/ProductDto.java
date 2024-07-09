package com.metinbudak.ecommerce.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;

import java.util.HashSet;
import java.util.Set;

public class ProductDto {

    private String category;
    private long categoryId;
    @NotBlank
    private String name;
    @Min(0)
    private double new_price;
    @Min(0)
    private double old_price;
    private Set<Long> imageIds = new HashSet<>();

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getNew_price() {
        return new_price;
    }

    public void setNew_price(double new_price) {
        this.new_price = new_price;
    }

    public double getOld_price() {
        return old_price;
    }

    public void setOld_price(double old_price) {
        this.old_price = old_price;
    }

    public Set<Long> getImageIds() {
        return imageIds;
    }

    public void setImageIds(Set<Long> imageIds) {
        this.imageIds = imageIds;
    }

}
