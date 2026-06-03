package com.krisnaajiep.urlshortening.service;

import io.seruco.encoding.base62.Base62;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class Base62UrlShortenerTest {
    private Base62 base62;

    private Base62UrlShortener urlShortener;

    @BeforeEach
    void setUp() {
        base62 = Base62.createInstance();
        urlShortener = new Base62UrlShortener(base62);
    }

    @AfterEach
    void tearDown() {
    }

    @Test
    void shorten_withShortUrlId_shouldReturnBase62Encoded() {
        long id = 1L;
        String shortened = urlShortener.shorten(id);

        assertNotNull(shortened);

        byte[] decoded = base62.decode(shortened.getBytes());
        long parsed = Long.parseLong(new String(decoded));

        assertEquals(id, parsed);
    }
}