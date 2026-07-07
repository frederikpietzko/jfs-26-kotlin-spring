package com.example.petclinic.api.dto;

import java.util.Set;

public record OwnerResponse(
    Long id,
    String firstName,
    String lastName,
    String address,
    String city,
    String telephone,
    Set<PetResponse> pets
) {
}
