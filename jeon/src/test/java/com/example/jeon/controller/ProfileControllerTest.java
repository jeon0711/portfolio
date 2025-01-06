package com.example.jeon.controller;

import com.example.jeon.service.ProfileService;
import com.example.jeon.dto.*;
import com.example.jeon.repository.UserProfileRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.web.context.WebApplicationContext;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.request;

@SpringBootTest
@AutoConfigureMockMvc
public class ProfileControllerTest {
    @Autowired
    protected MockMvc mockMvc;
    @Autowired
    protected ObjectMapper objectMapper;
    @Autowired
    private WebApplicationContext context;
    @Autowired
    UserProfileRepository userProfileRepository;
    @Value("${aws.s3.bucket-name}")
    private String bucketName;
    @Value("${aws.region}")
    private String region;
    @Value("${aws.access-key-id}")
    private String accessKey;
    @Value("${aws.aws.secret-access-key}")
    private String secretKey;
    @Autowired
    private ProfileService profileService;
    @Test
    @DisplayName("S3upload테스트")
    public void uploadTest() throws Exception {
        MockMultipartFile mockImage = new MockMultipartFile(
                "image",
                "test.jpg",
                "image/jpeg",
                "mock image content".getBytes()
        );

        // JSON 데이터 생성
        AddUserProfile input = new AddUserProfile(
                "Test Title",
                "Test Content",
                "Test Name"
        );
        String inputJson = objectMapper.writeValueAsString(input);

        // JSON 데이터를 포함하는 MockMultipartFile 생성
        MockMultipartFile mockJson = new MockMultipartFile(
                "data", // @RequestPart의 이름과 동일해야 함
                "data.json",
                "application/json",
                inputJson.getBytes()
        );

        // 요청 및 검증
        mockMvc.perform(MockMvcRequestBuilders.multipart("/profile/")
                        .file(mockImage)
                        .file(mockJson) // JSON 데이터 추가
                        .contentType(MediaType.MULTIPART_FORM_DATA))
                .andExpect(status().isCreated());
    }
}
