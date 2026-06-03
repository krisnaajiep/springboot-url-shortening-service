package com.krisnaajiep.urlshortening.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.krisnaajiep.urlshortening.model.ShortUrl;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ShortUrlResponse {
    private Long id;
    private String url;
    private String shortCode;
    private Long accessCount;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public static ShortUrlResponse from(ShortUrl shortUrl, boolean includeStatistics) {
        return ShortUrlResponse.builder()
                .id(shortUrl.getId())
                .url(shortUrl.getUrl())
                .shortCode(shortUrl.getShortCode())
                .createdAt(shortUrl.getCreatedAt())
                .updatedAt(shortUrl.getUpdatedAt())
                .accessCount(includeStatistics ? shortUrl.getAccessCount() : null)
                .build();
    }
}
