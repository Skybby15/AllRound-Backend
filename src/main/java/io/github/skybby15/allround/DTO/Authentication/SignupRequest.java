package io.github.skybby15.allround.DTO.Authentication;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record SignupRequest(
    @Size(min = 3) @NotBlank String username,
    @Email @NotBlank String email,
    @Size(min = 6) @NotBlank String password) {}
