package net.eclearing;

import java.time.LocalDateTime;

public record Task(
    long id,
    LocalDateTime createdAt,
    LocalDateTime modifiedAt,
    LocalDateTime completedAt,
    LocalDateTime deletedAt,
    String title
) {}
