package com.metinbudak.ecommerce.exception;

public class InternalServerException extends RuntimeException {

    public InternalServerException(Exception e) {
        super(e.getMessage());
    }

    public InternalServerException(String message) {
        super(message);
    }

}