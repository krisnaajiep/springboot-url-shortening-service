package com.krisnaajiep.urlshortening.config;

import io.github.cdimascio.dotenv.Dotenv;
import io.github.cdimascio.dotenv.DotenvException;
import lombok.extern.slf4j.Slf4j;
import org.jspecify.annotations.NonNull;
import org.springframework.boot.context.event.ApplicationEnvironmentPreparedEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class DotenvLoader implements ApplicationListener<ApplicationEnvironmentPreparedEvent> {
    @Override
    public void onApplicationEvent(@NonNull ApplicationEnvironmentPreparedEvent event) {
        log.info("Dotenv loader started");

        if (event.getEnvironment().matchesProfiles("build", "test")) {
            log.info("Skipping dotenv loading for build and test profiles.");
            return;
        }

        try {
            log.info("Loading .env file");
            Dotenv.configure().systemProperties().load();
        } catch (DotenvException e) {
            log.error(e.getMessage());
        }

    }
}
