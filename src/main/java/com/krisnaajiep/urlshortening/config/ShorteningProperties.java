package com.krisnaajiep.urlshortening.config;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;
import org.springframework.validation.annotation.Validated;

@ConfigurationProperties(prefix = "shortening")
@Component
@Getter
@Setter
@Validated
public class ShorteningProperties {
    @NotNull
    @Positive
    private int maxRetries;
}
