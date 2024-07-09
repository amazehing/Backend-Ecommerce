package com.metinbudak.ecommerce.dto;

import jakarta.validation.constraints.Min;

public class CheckoutDto {

    @Min(0)
    private long productId;
    @Min(1)
    private int total;

    public long getProductId() {
        return productId;
    }

    public void setProductId(long productId) {
        this.productId = productId;
    }

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }

}
