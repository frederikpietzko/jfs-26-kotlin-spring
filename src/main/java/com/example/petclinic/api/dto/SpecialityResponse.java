package com.example.petclinic.api.dto;

import java.util.Set;
import java.util.stream.Collectors;

public class SpecialityResponse {
    private Long id;
    private String name;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public static SpecialityResponse fromEntity(com.example.petclinic.domain.entity.Speciality speciality) {
        SpecialityResponse response = new SpecialityResponse();
        response.setId(speciality.getId());
        response.setName(speciality.getName());
        return response;
    }

    public static Set<SpecialityResponse> fromEntities(Set<com.example.petclinic.domain.entity.Speciality> specialities) {
        if (specialities == null) {
            return Set.of();
        }
        return specialities.stream()
                .map(SpecialityResponse::fromEntity)
                .collect(Collectors.toSet());
    }
}
