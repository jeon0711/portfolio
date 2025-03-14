package com.example.jeon.util;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class StringParser {
    public static List<String> parseString(String skills) {
        if (skills == null || skills.trim().isEmpty()) {
            return List.of(); // ✅ `null` 또는 빈 문자열이면 빈 리스트 반환
        }

        return Arrays.stream(skills.split("[,\\s]+")) // ✅ 쉼표 `,` 또는 공백 ` ` 기준으로 나누기
                .map(String::trim) // ✅ 공백 제거
                .filter(s -> !s.isEmpty()) // ✅ 빈 문자열 제거
                .collect(Collectors.toList()); // ✅ 리스트로 변환
    }

}
