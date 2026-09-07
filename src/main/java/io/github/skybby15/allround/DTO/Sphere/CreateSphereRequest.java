package io.github.skybby15.allround.DTO.Sphere;

import jakarta.validation.constraints.NotBlank;

public record CreateSphereRequest(
    @NotBlank String name
){
    
}
