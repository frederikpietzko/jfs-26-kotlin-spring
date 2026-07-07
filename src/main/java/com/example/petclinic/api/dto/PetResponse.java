package com.example.petclinic.api.dto;

import java.time.LocalDate;
import java.util.Set;

public record PetResponse(
    Long id,
    String name,
    PetTypeRequest type,
    LocalDate birthDate,
    Set<VisitResponse> visits
) {
}
