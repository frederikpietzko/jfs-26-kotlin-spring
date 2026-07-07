package com.example.petclinic.api.dto;

import com.example.petclinic.domain.entity.Vet;
import java.util.Set;
import java.util.stream.Collectors;

public record VetResponse(
    Long id,
    String firstName,
    String lastName,
    Set<SpecialityResponse> specialties
) {
    public static VetResponse fromEntity(Vet vet) {
        Set<SpecialityResponse> specialties = null;
        if (vet.getSpecialties() != null) {
            specialties = vet.getSpecialties().stream()
                    .map(SpecialityResponse::fromEntity)
                    .collect(Collectors.toSet());
        }
        return new VetResponse(vet.getId(), vet.getFirstName(), vet.getLastName(), specialties);
    }
}
