package com.example.petclinic.api.dto;

import com.example.petclinic.domain.entity.Visit;

import java.time.LocalDate;

public record VisitResponse(
        Long id,
        LocalDate date,
        String description
) {
    public static VisitResponse fromEntity(Visit visit) {
        return new VisitResponse(visit.getId(), visit.getDate(), visit.getDescription());
    }
}
