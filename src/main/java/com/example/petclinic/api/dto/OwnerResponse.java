package com.example.petclinic.api.dto;

import com.example.petclinic.domain.entity.Owner;
import java.util.Set;
import java.util.stream.Collectors;

public record OwnerResponse(
    Long id,
    String firstName,
    String lastName,
    String address,
    String city,
    String telephone,
    Set<PetResponse> pets
) {
    public static OwnerResponse fromEntity(Owner owner) {
        Set<PetResponse> pets = null;
        if (owner.getPets() != null) {
            pets = owner.getPets().stream()
                    .map(PetResponse::fromEntity)
                    .collect(Collectors.toSet());
        }
        return new OwnerResponse(owner.getId(), owner.getFirstName(), owner.getLastName(),
            owner.getAddress(), owner.getCity(), owner.getTelephone(), pets);
    }
}
