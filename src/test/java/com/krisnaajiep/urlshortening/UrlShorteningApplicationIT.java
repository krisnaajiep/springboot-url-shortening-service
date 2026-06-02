package com.krisnaajiep.urlshortening;

import com.krisnaajiep.urlshortening.config.ITConfig;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;

@SpringBootTest
@Import(ITConfig.class)
class UrlShorteningApplicationIT {

    @Test
    void contextLoads() {
    }

}
