package com.example.petclinic.api.dto;

import java.util.Set;

public record VetResponse(
    Long id,
    String firstName,
    String lastName,
    Set<SpecialityResponse> specialties
) {
}
