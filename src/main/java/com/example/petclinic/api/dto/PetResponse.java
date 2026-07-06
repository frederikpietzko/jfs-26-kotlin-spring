package com.example.petclinic.api.dto;

import java.time.LocalDate;
import java.util.Set;
import java.util.stream.Collectors;

public class PetResponse {
    private Long id;
    private String name;
    private PetTypeRequest type;
    private LocalDate birthDate;
    private Set<VisitResponse> visits;

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

    public PetTypeRequest getType() {
        return type;
    }

    public void setType(PetTypeRequest type) {
        this.type = type;
    }

    public LocalDate getBirthDate() {
        return birthDate;
    }

    public void setBirthDate(LocalDate birthDate) {
        this.birthDate = birthDate;
    }

    public Set<VisitResponse> getVisits() {
        return visits;
    }

    public void setVisits(Set<VisitResponse> visits) {
        this.visits = visits;
    }

    public static PetResponse fromEntity(com.example.petclinic.domain.entity.Pet pet) {
        PetResponse response = new PetResponse();
        response.setId(pet.getId());
        response.setName(pet.getName());
        response.setType(PetTypeRequest.valueOf(pet.getType().name()));
        response.setBirthDate(pet.getBirthDate());
        if (pet.getVisits() != null) {
            response.setVisits(pet.getVisits().stream()
                    .map(VisitResponse::fromEntity)
                    .collect(Collectors.toSet()));
        }
        return response;
    }
}
