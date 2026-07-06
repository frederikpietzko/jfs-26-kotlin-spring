package com.example.petclinic.api.dto;

import jakarta.validation.constraints.NotBlank;

public class SpecialityRequest {
    @NotBlank
    private String name;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
