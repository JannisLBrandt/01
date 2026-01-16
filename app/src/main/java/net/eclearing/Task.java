package net.eclearing;

import java.time.LocalDateTime;

public record Task(
    long id,
    LocalDateTime createdAt,
    LocalDateTime modified_at,
    LocalDateTime completed_at,
    LocalDateTime deleted_at,
    String title
) {}
