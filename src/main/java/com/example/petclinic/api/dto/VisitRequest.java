package com.example.petclinic.api.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDate;

public record VisitRequest(
    @NotNull
    Long petId,

    LocalDate date,

    @NotBlank
    String description
) {
}
