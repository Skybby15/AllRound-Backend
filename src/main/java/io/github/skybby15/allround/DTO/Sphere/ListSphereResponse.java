package io.github.skybby15.allround.DTO.Sphere;

import java.util.List;

import lombok.Builder;

@Builder 
public record ListSphereResponse(
    List<SphereListInfoDTO> spheres
) {
    
}
