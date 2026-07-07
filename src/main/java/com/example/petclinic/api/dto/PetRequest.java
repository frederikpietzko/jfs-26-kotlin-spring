package com.example.petclinic.api.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record PetRequest(
    @NotBlank
    String name,

    @NotNull
    PetTypeRequest type
) {
}
