package io.github.skybby15.allround.DTO.Authentication;

import lombok.Builder;

@Builder
public record LoginResponse(String authToken, String tokenType, Long expiresIn) {}
