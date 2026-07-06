package com.example.petclinic.api.dto;

import jakarta.validation.constraints.NotBlank;
import java.time.LocalDate;

public class VisitRequest {
    private LocalDate date;

    @NotBlank
    private String description;

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
