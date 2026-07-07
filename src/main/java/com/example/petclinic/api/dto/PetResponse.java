package com.example.petclinic.api.dto;

import com.example.petclinic.domain.entity.Pet;
import java.time.LocalDate;
import java.util.Set;
import java.util.stream.Collectors;

public record PetResponse(
    Long id,
    String name,
    PetTypeRequest type,
    LocalDate birthDate,
    Set<VisitResponse> visits
) {
    public static PetResponse fromEntity(Pet pet) {
        Set<VisitResponse> visits = null;
        if (pet.getVisits() != null) {
            visits = pet.getVisits().stream()
                    .map(VisitResponse::fromEntity)
                    .collect(Collectors.toSet());
        }
        return new PetResponse(pet.getId(), pet.getName(), 
            PetTypeRequest.valueOf(pet.getType().name()), pet.getBirthDate(), visits);
    }
}
