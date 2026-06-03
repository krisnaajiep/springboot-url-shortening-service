package com.krisnaajiep.urlshortening.service;

import com.krisnaajiep.urlshortening.config.ShorteningProperties;
import com.krisnaajiep.urlshortening.controller.InternalServerErrorException;
import com.krisnaajiep.urlshortening.controller.NotFoundException;
import com.krisnaajiep.urlshortening.dto.ShortUrlRequest;
import com.krisnaajiep.urlshortening.dto.ShortUrlResponse;
import com.krisnaajiep.urlshortening.model.ShortUrl;
import com.krisnaajiep.urlshortening.model.ShortUrlRepository;
import org.instancio.Instancio;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.mockito.ArgumentMatchers.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ShortUrlServiceTest {

    @Mock
    private ShortUrlRepository shortUrlRepository;

    @Mock
    private UrlShortener urlShortener;

    @Mock
    private ShorteningProperties shorteningProperties;

    @InjectMocks
    private ShortUrlService shortUrlService;

    @BeforeEach
    void setUp() {
    }

    @AfterEach
    void tearDown() {
    }

    @Test
    void create_withDuplicateShortCodeMoreThanMaxRetries_shouldThrowInternalServerErrorException() {
        int maxRetries = 5;
        ShortUrl shortUrl = Instancio.create(ShortUrl.class);
        ShortUrlRequest request = Instancio.create(ShortUrlRequest.class);

        when(shortUrlRepository.save(any(ShortUrl.class))).thenReturn(shortUrl);
        when(urlShortener.shorten(anyLong())).thenReturn("shorten");
        when(shortUrlRepository.existsByShortCode(anyString())).thenReturn(true);
        when(shorteningProperties.getMaxRetries()).thenReturn(maxRetries);

        assertThrows(InternalServerErrorException.class, () -> shortUrlService.create(request));

        verify(shortUrlRepository, times(1)).save(any(ShortUrl.class));
        verify(urlShortener, times(maxRetries)).shorten(anyLong());
        verify(shorteningProperties, times(maxRetries)).getMaxRetries();
        verify(shortUrlRepository, times(maxRetries)).existsByShortCode(anyString());
        verifyNoMoreInteractions(shortUrlRepository, urlShortener, shorteningProperties);
    }

    @Test
    void create_withDuplicateShortCodeLessThanMaxRetries_shouldReturnShortUrlResponse() {
        int maxRetries = 5;
        String duplicateShorten = "duplicateShorten";
        String uniqueShorten = "uniqueShorten";
        ShortUrl shortUrl = Instancio.create(ShortUrl.class);
        ShortUrlRequest request = Instancio.create(ShortUrlRequest.class);

        when(shortUrlRepository.save(any(ShortUrl.class))).thenReturn(shortUrl);
        when(urlShortener.shorten(anyLong())).thenReturn(duplicateShorten, duplicateShorten, uniqueShorten);
        when(shortUrlRepository.existsByShortCode(anyString())).thenReturn(true, true, false);
        when(shorteningProperties.getMaxRetries()).thenReturn(maxRetries);

        ShortUrlResponse response = shortUrlService.create(request);
        assertEquals(uniqueShorten, response.getShortCode());

        verify(shortUrlRepository, times(1)).save(any(ShortUrl.class));
        verify(urlShortener, times(3)).shorten(anyLong());
        verify(shortUrlRepository, times(3)).existsByShortCode(anyString());
        verify(shorteningProperties, times(2)).getMaxRetries();
        verifyNoMoreInteractions(shortUrlRepository, urlShortener, shorteningProperties);
    }

    @Test
    void create_withNoDuplicateShortCode_shouldReturnShortUrlResponse() {
        String shorten = "shorten";
        ShortUrl shortUrl = Instancio.create(ShortUrl.class);
        ShortUrlRequest request = Instancio.create(ShortUrlRequest.class);

        when(shortUrlRepository.save(any(ShortUrl.class))).thenReturn(shortUrl);
        when(urlShortener.shorten(anyLong())).thenReturn(shorten);
        when(shortUrlRepository.existsByShortCode(anyString())).thenReturn(false);

        ShortUrlResponse response = shortUrlService.create(request);
        assertEquals(shorten, response.getShortCode());

        verify(shortUrlRepository, times(1)).save(any(ShortUrl.class));
        verify(urlShortener, times(1)).shorten(anyLong());
        verify(shortUrlRepository, times(1)).existsByShortCode(anyString());
        verifyNoMoreInteractions(shortUrlRepository, urlShortener);
        verifyNoInteractions(shorteningProperties);
    }

    @Test
    void retrieve_withNonExistingShortCode_shouldThrowNotFoundException() {
        when(shortUrlRepository.findByShortCode(anyString())).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> shortUrlService.retrieve("non-existing-short-code"));

        verify(shortUrlRepository, times(1)).findByShortCode(anyString());
        verifyNoMoreInteractions(shortUrlRepository);
    }

    @Test
    void retrieve_withExistingShortCode_shouldReturnShortUrlResponse() {
        ShortUrl shortUrl = Instancio.create(ShortUrl.class);

        when(shortUrlRepository.findByShortCode(anyString())).thenReturn(Optional.of(shortUrl));

        ShortUrlResponse response = shortUrlService.retrieve(shortUrl.getShortCode());
        assertEquals(shortUrl.getShortCode(), response.getShortCode());

        verify(shortUrlRepository, times(1)).findByShortCode(anyString());
        verifyNoMoreInteractions(shortUrlRepository);
    }
}