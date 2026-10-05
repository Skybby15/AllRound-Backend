package io.github.skybby15.allround.DTO.Sphere.List;

import lombok.Builder;

@Builder
public record SphereListInfoDTO(Long id, Long ownerId, String name) {}
