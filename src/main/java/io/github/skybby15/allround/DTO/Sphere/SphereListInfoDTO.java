package io.github.skybby15.allround.DTO.Sphere;

import lombok.Builder;

@Builder 
public record SphereListInfoDTO(
  Long id,
  Long ownerId,
  String name
) {
    
}
