package com.example.petclinic.api.dto;

import java.time.LocalDate;

public record VisitResponse(
        Long id,
        LocalDate date,
        String description
) {
}
