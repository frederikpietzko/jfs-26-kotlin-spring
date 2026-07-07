package com.example.petclinic.api.dto;

import com.example.petclinic.domain.entity.Speciality;
import java.util.Set;
import java.util.stream.Collectors;

public record SpecialityResponse(
    Long id,
    String name
) {
    public static SpecialityResponse fromEntity(Speciality speciality) {
        return new SpecialityResponse(speciality.getId(), speciality.getName());
    }

    public static Set<SpecialityResponse> fromEntities(Set<Speciality> specialities) {
        if (specialities == null) {
            return Set.of();
        }
        return specialities.stream()
                .map(SpecialityResponse::fromEntity)
                .collect(Collectors.toSet());
    }
}
