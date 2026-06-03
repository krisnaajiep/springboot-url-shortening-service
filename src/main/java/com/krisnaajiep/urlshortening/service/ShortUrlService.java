package com.krisnaajiep.urlshortening.service;

import com.krisnaajiep.urlshortening.config.ShorteningProperties;
import com.krisnaajiep.urlshortening.controller.InternalServerErrorException;
import com.krisnaajiep.urlshortening.controller.NotFoundException;
import com.krisnaajiep.urlshortening.dto.ShortUrlRequest;
import com.krisnaajiep.urlshortening.dto.ShortUrlResponse;
import com.krisnaajiep.urlshortening.model.ShortUrl;
import com.krisnaajiep.urlshortening.model.ShortUrlRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ShortUrlService {
    private final ShortUrlRepository shortUrlRepository;
    private final UrlShortener urlShortener;
    private final ShorteningProperties shorteningProperties;

    @Transactional
    public ShortUrlResponse create(ShortUrlRequest request) {
        ShortUrl saved = shortUrlRepository.save(new ShortUrl(request.getUrl()));
        String shortCode = urlShortener.shorten(saved.getId());

        int retries = 1;
        while (shortUrlRepository.existsByShortCode(shortCode)) {
            int maxRetries = shorteningProperties.getMaxRetries();
            if (retries >= maxRetries) {
                throw new InternalServerErrorException(
                        "Failed to generate unique short code after " + maxRetries + " retries"
                );
            }

            shortCode = urlShortener.shorten(saved.getId());
            retries++;
        }

        saved.setShortCode(shortCode);

        return ShortUrlResponse.from(saved, false);
    }

    @Transactional
    public ShortUrlResponse retrieve(String shortCode) {
        ShortUrl shortUrl = shortUrlRepository.findByShortCode(shortCode).orElseThrow(
                () -> new NotFoundException("Short code not found: " + shortCode)
        );

        shortUrl.incrementAccessCount();

        return ShortUrlResponse.from(shortUrl, false);
    }

    @Transactional
    public ShortUrlResponse update(String shortCode, ShortUrlRequest request) {
        ShortUrl shortUrl = shortUrlRepository.findByShortCode(shortCode).orElseThrow(
                () -> new NotFoundException("Short code not found: " + shortCode)
        );

        shortUrl.setUrl(request.getUrl());

        return ShortUrlResponse.from(shortUrl, false);
    }

    @Transactional
    public void delete(String shortCode) {
        ShortUrl shortUrl = shortUrlRepository.findByShortCode(shortCode).orElseThrow(
                () -> new NotFoundException("Short code not found: " + shortCode)
        );

        shortUrlRepository.delete(shortUrl);
    }

    @Transactional(readOnly = true)
    public ShortUrlResponse getStats(String shortCode) {
        ShortUrl shortUrl = shortUrlRepository.findByShortCode(shortCode).orElseThrow(
                () ->  new NotFoundException("Short code not found: " + shortCode)
        );

        return ShortUrlResponse.from(shortUrl, true);
    }
}
