package com.krisnaajiep.urlshortening.model;

import org.springframework.data.jpa.repository.JpaRepository;

public interface ShortUrlRepository extends JpaRepository<ShortUrl, Long> {
    boolean existsByShortCode(String shortCode);
    ShortUrl findByShortCode(String shortCode);
}
