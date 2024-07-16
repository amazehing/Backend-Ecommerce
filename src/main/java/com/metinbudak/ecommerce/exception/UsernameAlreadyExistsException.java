package com.metinbudak.ecommerce.exception;

public class UsernameAlreadyExistsException extends RuntimeException {

    public UsernameAlreadyExistsException() {
        super();
    }

    public UsernameAlreadyExistsException(String username) {
        super("The provided username '%s' has already been taken.".formatted(username));
    }
}