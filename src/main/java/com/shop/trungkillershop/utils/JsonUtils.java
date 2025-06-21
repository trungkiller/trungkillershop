package com.shop.trungkillershop.utils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;

public class JsonUtils {
    private static final ObjectMapper camelCaseMapper = new ObjectMapper();

    static {
        // Convert keys to camelCase automatically
        camelCaseMapper.setPropertyNamingStrategy(PropertyNamingStrategies.SNAKE_CASE);
    }

    public static String toCamelCaseJson(Object obj) {
        try {
            return camelCaseMapper.writeValueAsString(obj);
        } catch (JsonProcessingException e) {
            throw new RuntimeException("JSON conversion failed", e);
        }
    }

    public static ObjectMapper getCamelCaseMapper() {
        return camelCaseMapper;
    }
}
