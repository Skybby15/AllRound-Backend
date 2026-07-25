package io.github.skybby15.allround.DTO.Authentication;

import lombok.Builder;
import org.eclipse.microprofile.openapi.annotations.media.Schema;

@Builder
public record LoginResponse(@Schema(required = true) String accessToken) {}
