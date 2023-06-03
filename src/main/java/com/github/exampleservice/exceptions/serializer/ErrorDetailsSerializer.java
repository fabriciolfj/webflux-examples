package com.github.exampleservice.exceptions.serializer;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.github.exampleservice.exceptions.handle.ErrorDetails;

import java.io.IOException;

public class ErrorDetailsSerializer extends JsonSerializer<ErrorDetails> {
    @Override
    public void serialize(ErrorDetails value, JsonGenerator gen, SerializerProvider serializers) throws IOException {
        gen.writeStartObject();
        gen.writeStringField("code", value.getErrorCode().toString());
        gen.writeStringField("message", value.getErrorMessage());
        gen.writeStringField("reference", value.getReferenceUrl());
        gen.writeEndObject();
    }
}