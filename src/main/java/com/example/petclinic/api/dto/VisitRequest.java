package com.example.petclinic.api.dto;

import jakarta.validation.constraints.NotBlank;
import java.time.LocalDate;

public record VisitRequest(
    LocalDate date,

    @NotBlank
    String description
) {
}
