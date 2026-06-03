package com.krisnaajiep.urlshortening.model;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ShortUrlRepository extends JpaRepository<ShortUrl, Long> {
    boolean existsByShortCode(String shortCode);
    Optional<ShortUrl> findByShortCode(String shortCode);
}
