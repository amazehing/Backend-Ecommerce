package com.metinbudak.ecommerce.exception;

public class ImageNotFoundException extends RuntimeException {

    public ImageNotFoundException() {
        this("Image not found!");
    }
    public ImageNotFoundException(String message) {
        super(message);
    }
}