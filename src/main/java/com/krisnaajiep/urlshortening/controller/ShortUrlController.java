package com.krisnaajiep.urlshortening.controller;

import com.krisnaajiep.urlshortening.dto.ShortUrlRequest;
import com.krisnaajiep.urlshortening.dto.ShortUrlResponse;
import com.krisnaajiep.urlshortening.service.ShortUrlService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;

@RestController
@RequestMapping("/shorten")
@RequiredArgsConstructor
public class ShortUrlController {
    private final ShortUrlService shortUrlService;

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ShortUrlResponse> create(@Valid @RequestBody ShortUrlRequest shortUrlRequest) {
        ShortUrlResponse response = shortUrlService.create(shortUrlRequest);
        return ResponseEntity.created(URI.create("/shorten/" + response.getShortCode())).body(response);
    }

    @GetMapping(value = "/{shortCode}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ShortUrlResponse> retrieve(@PathVariable("shortCode") String shortCode) {
        ShortUrlResponse response = shortUrlService.retrieve(shortCode);
        return ResponseEntity.ok(response);
    }
}
