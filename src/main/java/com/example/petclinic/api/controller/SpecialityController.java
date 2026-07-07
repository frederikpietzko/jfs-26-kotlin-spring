package com.example.petclinic.api.controller;

import com.example.petclinic.api.dto.SpecialityRequest;
import com.example.petclinic.api.dto.SpecialityResponse;
import com.example.petclinic.domain.entity.Speciality;
import com.example.petclinic.service.SpecialityService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashSet;
import java.util.List;

@RestController
@RequestMapping("/specialities")
@RequiredArgsConstructor
public class SpecialityController {

    private final SpecialityService specialityService;

    @GetMapping
    public List<SpecialityResponse> getAll() {
        return specialityService.findAll().stream()
                .map(SpecialityResponse::fromEntity)
                .collect(java.util.stream.Collectors.toList());
    }

    @GetMapping("/{id}")
    public ResponseEntity<SpecialityResponse> getById(@PathVariable Long id) {
        final var speciality = specialityService.findById(id);
        if (speciality == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(SpecialityResponse.fromEntity(speciality));
    }

    @PostMapping
    public ResponseEntity<SpecialityResponse> create(@Valid @RequestBody SpecialityRequest request) {
        final var entity = new Speciality(null, request.name(), new HashSet<>());

        final var saved = specialityService.save(entity);
        return ResponseEntity.status(HttpStatus.CREATED).body(SpecialityResponse.fromEntity(saved));
    }

    @PutMapping("/{id}")
    public ResponseEntity<SpecialityResponse> update(@PathVariable Long id, @Valid @RequestBody SpecialityRequest request) {
        final var entity = new Speciality(null, request.name(), new HashSet<>());

        final var updated = specialityService.update(id, entity);
        if (updated == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(SpecialityResponse.fromEntity(updated));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        specialityService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
