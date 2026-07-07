package com.example.petclinic.api.dto;

import jakarta.validation.constraints.NotBlank;
import java.util.List;

public record VetRequest(
    @NotBlank
    String firstName,

    @NotBlank
    String lastName,

    List<Long> specialityIds
) {
}
