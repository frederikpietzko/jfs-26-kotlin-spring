package com.example.petclinic.api.dto;

import jakarta.validation.constraints.NotBlank;
import java.util.List;

public class VetRequest {
    @NotBlank
    private String firstName;

    @NotBlank
    private String lastName;

    private List<Long> specialityIds;

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

    public List<Long> getSpecialityIds() {
        return specialityIds;
    }

    public void setSpecialityIds(List<Long> specialityIds) {
        this.specialityIds = specialityIds;
    }
}
