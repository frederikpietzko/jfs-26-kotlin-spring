package com.example.petclinic.api.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

public record OwnerRequest(
    @NotBlank
    String firstName,

    @NotBlank
    String lastName,

    String address,

    String city,

    @NotBlank
    @Pattern(regexp = "\\d{9}", message = "Telephone must be 9 digits")
    String telephone
) {
}
