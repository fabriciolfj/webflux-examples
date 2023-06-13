package com.github.exampleservice.controller.dto.error;

public enum StatusCode {

    EC001("given number is not within range"),
    EC002("you usage limit exceeded");

    private final String description;

    StatusCode(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }
}
