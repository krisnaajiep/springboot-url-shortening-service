CREATE TABLE short_urls
(
    id           BIGINT      NOT NULL PRIMARY KEY AUTO_INCREMENT,
    url          TEXT        NOT NULL,
    short_code   VARCHAR(10) NULL UNIQUE,
    access_count BIGINT      NOT NULL DEFAULT 0,
    created_at   TIMESTAMP   NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at   TIMESTAMP   NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);

CREATE INDEX idx_short_code ON short_urls (short_code);