package com.example.petclinic.api.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public class PetRequest {
    @NotBlank
    private String name;

    @NotNull
    private PetTypeRequest type;

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
}
