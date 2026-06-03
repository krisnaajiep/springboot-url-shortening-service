package com.krisnaajiep.urlshortening.service;

import io.seruco.encoding.base62.Base62;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
class Base62UrlShortener implements UrlShortener {
    private final Base62 base62;

    @Override
    public String shorten(Long id) {
        byte[] encoded = base62.encode(id.toString().getBytes());
        return new String(encoded);
    }
}
