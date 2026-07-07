package com.example.petclinic.api.dto;

import jakarta.validation.constraints.NotBlank;

public record SpecialityRequest(
    @NotBlank
    String name
) {
}
