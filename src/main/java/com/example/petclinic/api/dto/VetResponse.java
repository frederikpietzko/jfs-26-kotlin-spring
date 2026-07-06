package com.example.petclinic.api.dto;

import java.util.Set;
import java.util.stream.Collectors;

public class VetResponse {
    private Long id;
    private String firstName;
    private String lastName;
    private Set<SpecialityResponse> specialties;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public Set<SpecialityResponse> getSpecialties() {
        return specialties;
    }

    public void setSpecialties(Set<SpecialityResponse> specialties) {
        this.specialties = specialties;
    }

    public static VetResponse fromEntity(com.example.petclinic.domain.entity.Vet vet) {
        VetResponse response = new VetResponse();
        response.setId(vet.getId());
        response.setFirstName(vet.getFirstName());
        response.setLastName(vet.getLastName());
        if (vet.getSpecialties() != null) {
            response.setSpecialties(vet.getSpecialties().stream()
                    .map(SpecialityResponse::fromEntity)
                    .collect(Collectors.toSet()));
        }
        return response;
    }
}
