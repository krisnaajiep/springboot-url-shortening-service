package com.krisnaajiep.urlshortening.dto;

import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import static org.junit.jupiter.api.Assertions.*;

class ShortUrlRequestTest {
    private Validator validator;
    private ShortUrlRequest shortUrlRequest;

    @BeforeEach
    void setUp() {
        try (ValidatorFactory factory = Validation.buildDefaultValidatorFactory()) {
            validator = factory.getValidator();
        }
    }

    @AfterEach
    void tearDown() {
    }

    @Test
    void setUrl_withNullValue_shouldHasViolations() {
        shortUrlRequest = new ShortUrlRequest();

        assertHasViolations(shortUrlRequest);
        assertIsBlank(shortUrlRequest);
    }

    @ParameterizedTest
    @ValueSource(strings = {"", " ", "     "})
    void setUrl_withBlankValue_shouldHasViolations(String url) {
        shortUrlRequest = new ShortUrlRequest(url);

        assertHasViolations(shortUrlRequest);
        assertIsBlank(shortUrlRequest);
    }

    @ParameterizedTest
    @ValueSource(strings = {"invalid-url", "http:invalid-url"})
    void setUrl_withInvalidPattern_shouldHasViolations(String url) {
        shortUrlRequest = new ShortUrlRequest(url);

        assertHasViolations(shortUrlRequest);
        assertIsInvalidPattern(shortUrlRequest);
    }

    @ParameterizedTest
    @ValueSource(strings = {
            "http://example.com/hello/world",
            "https://www.dummyjson.com/products/best/sellers",
            "https://www.google.com/search?q=validation+test"
    })
    void setUrl_withValidUrl_shouldHasNoViolations(String url) {
        shortUrlRequest = new ShortUrlRequest(url);

        assertHasNoViolations(shortUrlRequest);
    }

    private void assertHasViolations(ShortUrlRequest request) {
        boolean anyMatch = validator.validate(request).stream().anyMatch(violation ->
                violation.getPropertyPath().toString().equals("url"));

        assertTrue(anyMatch);
    }

    private void assertIsBlank(ShortUrlRequest request) {
        boolean anyMatch = validator.validate(request).stream().anyMatch(violation ->
                violation.getConstraintDescriptor().getAnnotation().annotationType().equals(NotBlank.class));

        assertTrue(anyMatch);
    }

    private void assertIsInvalidPattern(ShortUrlRequest request) {
        boolean anyMatch = validator.validate(request).stream().anyMatch(violation ->
                violation.getConstraintDescriptor().getAnnotation().annotationType().equals(Pattern.class));

        assertTrue(anyMatch);
    }

    private void assertHasNoViolations(ShortUrlRequest request) {
        assertTrue(validator.validate(request).isEmpty());
    }
}