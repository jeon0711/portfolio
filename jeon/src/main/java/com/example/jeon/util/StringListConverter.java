package com.example.jeon.util;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;
import java.io.IOException;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

    @Converter(autoApply = true)
    public class StringListConverter implements AttributeConverter<List<String>, String> {
        private static final ObjectMapper objectMapper = new ObjectMapper();

        @Override
        public String convertToDatabaseColumn(List<String> attribute) {
            if (attribute == null || attribute.isEmpty()) {
                return "[]"; // 빈 리스트를 JSON 배열로 저장
            }
            try {
                return objectMapper.writeValueAsString(attribute);
            } catch (JsonProcessingException e) {
                throw new RuntimeException("JSON 변환 오류: " + e.getMessage(), e);
            }
        }

        @Override
        public List<String> convertToEntityAttribute(String dbData) {
            if (dbData == null || dbData.trim().isEmpty()) {
                return Collections.emptyList(); // 빈 리스트 반환
            }
            try {
                return objectMapper.readValue(dbData, List.class);
            } catch (IOException e) {
                throw new RuntimeException("DB 데이터 JSON 변환 오류: " + dbData, e);
            }
        }
    }
