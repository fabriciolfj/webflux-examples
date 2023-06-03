package com.github.exampleservice.exceptions;

public class UserNotFoundException extends RuntimeException {

    public UserNotFoundException(final String msg) {
        super(msg);
    }
}
