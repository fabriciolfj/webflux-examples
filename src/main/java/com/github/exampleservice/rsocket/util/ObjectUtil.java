package com.github.exampleservice.rsocket.util;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.rsocket.Payload;
import io.rsocket.util.DefaultPayload;

import java.util.Objects;

public class ObjectUtil {

    private ObjectMapper objectMapper;
    public static Payload toPayload(final Object o) {
        try {
            final ObjectMapper objectMapper = ObjectMapperUtil.getObjectMapper();
            final byte[] bytes = objectMapper.writeValueAsBytes(o);

            return DefaultPayload.create(bytes);
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    public static <T> T toObject(final Payload payload, final Class<T> type) {
        try {
            final ObjectMapper objectMapper = ObjectMapperUtil.getObjectMapper();
            final byte[] bytes = payload.getData().array();
            return objectMapper.readValue(bytes, type);
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }
    }

}
