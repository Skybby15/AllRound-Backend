package io.github.skybby15.allround.DTO.Sphere.List;

import java.util.List;
import lombok.Builder;

@Builder
public record ListSphereResponse(List<SphereListInfoDTO> spheres) {}
