package com.example.petclinic.api.dto;

import java.time.LocalDate;

public class VisitResponse {
    private Long id;
    private LocalDate date;
    private String description;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

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

    public static VisitResponse fromEntity(com.example.petclinic.domain.entity.Visit visit) {
        VisitResponse response = new VisitResponse();
        response.setId(visit.getId());
        response.setDate(visit.getDate());
        response.setDescription(visit.getDescription());
        return response;
    }
}
