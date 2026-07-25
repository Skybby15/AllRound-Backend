package io.github.skybby15.allround.DTO;

import java.time.Instant;

public record ErrorResponse(String code, String message, Instant timestamp) {}
