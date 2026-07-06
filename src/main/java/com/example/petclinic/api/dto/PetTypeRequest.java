package com.example.petclinic.api.dto;

import com.example.petclinic.domain.entity.PetType;

public enum PetTypeRequest {
    CAT, DOG, BIRD, FISH, RABBIT, HAMSTER, OTHER;

    public PetType toEntity() {
        return PetType.valueOf(this.name());
    }
}
