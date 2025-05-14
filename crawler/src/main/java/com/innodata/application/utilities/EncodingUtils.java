package com.innodata.application.utilities;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import com.fasterxml.jackson.databind.ObjectMapper;

public class EncodingUtils {

    private static final ObjectMapper objectMapper = new ObjectMapper();

    // Encodes a JSON object for safe URL usage
    public static String encodeJson(Object jsonData) {
        try {
            String jsonString = objectMapper.writeValueAsString(jsonData);
            return URLEncoder.encode(jsonString, StandardCharsets.UTF_8);
        } catch (Exception e) {
            throw new RuntimeException("Error encoding JSON", e);
        }
    }
}
