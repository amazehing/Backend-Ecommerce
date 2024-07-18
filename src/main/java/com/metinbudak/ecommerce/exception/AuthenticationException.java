package com.metinbudak.ecommerce.exception;

public class AuthenticationException extends RuntimeException {

    public AuthenticationException() {
        this("Invalid username or password!");
    }

    public AuthenticationException(String message) {
        super(message);
    }
}