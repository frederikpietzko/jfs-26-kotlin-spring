package com.example.petclinic.api.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record PetRequest(
    @NotNull
    Long ownerId,

    @NotBlank
    String name,

    @NotNull
    PetTypeRequest type
) {
}
