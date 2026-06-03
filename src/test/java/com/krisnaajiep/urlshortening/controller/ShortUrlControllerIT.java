package com.krisnaajiep.urlshortening.controller;

import com.krisnaajiep.urlshortening.config.ITConfig;
import com.krisnaajiep.urlshortening.dto.ShortUrlRequest;
import com.krisnaajiep.urlshortening.dto.ShortUrlResponse;
import com.krisnaajiep.urlshortening.model.ShortUrl;
import com.krisnaajiep.urlshortening.model.ShortUrlRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.context.annotation.Import;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ProblemDetail;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.transaction.annotation.Transactional;
import tools.jackson.core.type.TypeReference;
import tools.jackson.databind.ObjectMapper;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ActiveProfiles("test")
@SpringBootTest
@AutoConfigureMockMvc
@Transactional
@Import(ITConfig.class)
class ShortUrlControllerIT {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private ShortUrlRepository shortUrlRepository;

    @BeforeEach
    void setUp() {
    }

    @AfterEach
    void tearDown() {
    }

    @Test
    void create_withInvalidRequest_shouldReturn400() throws Exception {
        MvcResult result = mockMvc.perform(post("/shorten")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(new ShortUrlRequest("invalidUrl"))))
                .andExpectAll(status().isBadRequest())
                .andReturn();

        ProblemDetail response = objectMapper.readValue(
                result.getResponse().getContentAsString(),
                new TypeReference<>() {
                }
        );

        Map<String, Object> properties = response.getProperties();
        assertNotNull(properties);

        Map<String, String> errors = objectMapper.convertValue(
                properties.get("errors"),
                new TypeReference<>() {
                }
        );
        assertNotNull(errors.get("url"));
    }

    @Test
    void create_withValidRequest_shouldReturn201() throws Exception {
        String url = "https://example.com/hello/world";

        MvcResult result = mockMvc.perform(post("/shorten")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(new ShortUrlRequest(url))))
                .andExpectAll(status().isCreated())
                .andReturn();

        ShortUrlResponse response = objectMapper.readValue(
                result.getResponse().getContentAsString(),
                new TypeReference<>() {
                }
        );

        System.out.println(response);

        assertNotNull(response.getId());
        assertEquals(url, response.getUrl());
        assertNotNull(response.getShortCode());
        assertNotNull(response.getCreatedAt());
        assertNotNull(response.getUpdatedAt());
        assertNull(response.getAccessCount());
    }

    @Test
    void retrieve_withNonExistingShortCode_shouldReturn404() throws Exception {
        MvcResult result = mockMvc.perform(get("/shorten/{shortCode}", "12345"))
                .andExpectAll(status().isNotFound())
                .andReturn();

        ProblemDetail response = objectMapper.readValue(
                result.getResponse().getContentAsString(),
                new TypeReference<>() {
                }
        );

        assertEquals(HttpStatus.NOT_FOUND.value(), response.getStatus());
    }

    @Test
    void retrieve_withExistingShortCode_shouldReturn200() throws Exception {
        String url = "https://example.com/hello/world";

        MvcResult createResult = mockMvc.perform(post("/shorten")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(new ShortUrlRequest(url))))
                .andExpectAll(status().isCreated())
                .andReturn();

        ShortUrlResponse createResponse = objectMapper.readValue(
                createResult.getResponse().getContentAsString(),
                new TypeReference<>() {
                }
        );

        String shortCode = createResponse.getShortCode();

        MvcResult retrieveResult = mockMvc.perform(get("/shorten/{shortCode}", shortCode))
                .andExpectAll(status().isOk())
                .andReturn();

        ShortUrlResponse retrieveResponse = objectMapper.readValue(
                retrieveResult.getResponse().getContentAsString(),
                new TypeReference<>() {
                }
        );

        assertNotNull(retrieveResponse.getId());
        assertEquals(url, retrieveResponse.getUrl());
        assertEquals(shortCode, retrieveResponse.getShortCode());
        assertNotNull(retrieveResponse.getCreatedAt());
        assertNotNull(retrieveResponse.getUpdatedAt());
        assertNull(retrieveResponse.getAccessCount());

        ShortUrl shortUrl = shortUrlRepository.findByShortCode(retrieveResponse.getShortCode()).orElseThrow();

        assertEquals(1, shortUrl.getAccessCount());
    }

    @Test
    void update_withInvalidRequest_shouldReturn400() throws Exception {
        MvcResult result = mockMvc.perform(put("/shorten/{shortCode}", "12345")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(new ShortUrlRequest("invalidUrl"))))
                .andExpectAll(status().isBadRequest())
                .andReturn();

        ProblemDetail response = objectMapper.readValue(
                result.getResponse().getContentAsString(),
                new TypeReference<>() {
                }
        );

        Map<String, Object> properties = response.getProperties();
        assertNotNull(properties);

        Map<String, String> errors = objectMapper.convertValue(
                properties.get("errors"),
                new TypeReference<>() {
                }
        );

        assertNotNull(errors.get("url"));
    }

    @Test
    void update_withNonExistingShortCode_shouldReturn404() throws Exception {
        MvcResult result = mockMvc.perform(put("/shorten/{shortCode}", "12345")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(
                                new ShortUrlRequest("https://newurl.com/hello/world"))))
                .andExpectAll(status().isNotFound())
                .andReturn();

        ProblemDetail response = objectMapper.readValue(
                result.getResponse().getContentAsString(),
                new TypeReference<>() {
                }
        );

        assertEquals(HttpStatus.NOT_FOUND.value(), response.getStatus());
    }

    @Test
    void update_withValidRequestAndExistingShortCode_shouldReturn200() throws Exception {
        String url = "https://example.com/hello/world";
        String newUrl = "https://newurl.com/hello/world";

        MvcResult createResult = mockMvc.perform(post("/shorten")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(new ShortUrlRequest(url))))
                .andExpectAll(status().isCreated())
                .andReturn();

        ShortUrlResponse createResponse = objectMapper.readValue(
                createResult.getResponse().getContentAsString(),
                new TypeReference<>() {
                }
        );

        String shortCode = createResponse.getShortCode();

        MvcResult updateResult = mockMvc.perform(put("/shorten/{shortCode}", shortCode)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(new ShortUrlRequest(newUrl))))
                .andExpectAll(status().isOk())
                .andReturn();

        ShortUrlResponse updateResponse = objectMapper.readValue(
                updateResult.getResponse().getContentAsString(),
                new TypeReference<>() {
                }
        );

        assertNotNull(updateResponse.getId());
        assertEquals(newUrl, updateResponse.getUrl());
        assertEquals(shortCode, updateResponse.getShortCode());
        assertNotNull(updateResponse.getCreatedAt());
        assertNotNull(updateResponse.getUpdatedAt());
        assertNull(updateResponse.getAccessCount());

        ShortUrl shortUrl = shortUrlRepository.findByShortCode(shortCode).orElseThrow();

        assertEquals(updateResponse.getUrl(), shortUrl.getUrl());
    }
}