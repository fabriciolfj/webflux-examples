package com.github.exampleservice.rsocket.util;

import com.fasterxml.jackson.databind.ObjectMapper;

public class ObjectMapperUtil {

    private static ObjectMapper mapper;

    public static ObjectMapper getObjectMapper() {
        if (mapper == null) {
            mapper = new ObjectMapper();
        }

        return mapper;
    }
}
