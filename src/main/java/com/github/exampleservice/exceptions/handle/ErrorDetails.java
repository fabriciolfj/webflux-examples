package com.github.exampleservice.exceptions.handle;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.github.exampleservice.exceptions.serializer.ErrorDetailsSerializer;
import lombok.Getter;

@JsonSerialize(using = ErrorDetailsSerializer.class)
public enum ErrorDetails {
    API_USER_NOT_FOUND(123, "User not found", "http://example.com/123");
    @Getter
    private Integer errorCode;
    @Getter
    private String errorMessage;
    @Getter
    private String referenceUrl;

    ErrorDetails(Integer errorCode, String errorMessage, String referenceUrl) {
        this.errorCode = errorCode;
        this.errorMessage = errorMessage;
        this.referenceUrl = referenceUrl;
    }
}