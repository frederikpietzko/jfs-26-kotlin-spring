package com.example.petclinic.api.controller;

import com.example.petclinic.api.dto.VetRequest;
import com.example.petclinic.api.dto.VetResponse;
import com.example.petclinic.domain.entity.Speciality;
import com.example.petclinic.domain.entity.Vet;
import com.example.petclinic.service.VetService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.HashSet;
import java.util.Set;

@RestController
@RequestMapping("/vets")
@RequiredArgsConstructor
public class VetController {

    private final VetService vetService;

    @GetMapping
    public List<VetResponse> getAll() {
        return vetService.findAll().stream()
                .map(VetResponse::fromEntity)
                .collect(java.util.stream.Collectors.toList());
    }

    @GetMapping("/{id}")
    public ResponseEntity<VetResponse> getById(@PathVariable Long id) {
        final var vet = vetService.findById(id);
        if (vet == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(VetResponse.fromEntity(vet));
    }

    @PostMapping
    public ResponseEntity<VetResponse> create(@Valid @RequestBody VetRequest request) {
        Set<Speciality> specialties = request.specialityIds() != null
                ? request.specialityIds().stream()
                .map(id -> new Speciality(id, null, new HashSet<>()))
                .collect(java.util.stream.Collectors.toSet())
                : Set.of();

        final var entity = new Vet(null, request.firstName(), request.lastName(), specialties);

        final var saved = vetService.save(entity);
        return ResponseEntity.status(HttpStatus.CREATED).body(VetResponse.fromEntity(saved));
    }

    @PutMapping("/{id}")
    public ResponseEntity<VetResponse> update(@PathVariable Long id, @Valid @RequestBody VetRequest request) {
        Set<Speciality> specialties = request.specialityIds() != null
                ? request.specialityIds().stream()
                .map(sId -> new Speciality(sId, null, new HashSet<>()))
                .collect(java.util.stream.Collectors.toSet())
                : Set.of();

        final var entity = new Vet(null, request.firstName(), request.lastName(), specialties);

        final var updated = vetService.update(id, entity);
        if (updated == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(VetResponse.fromEntity(updated));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        vetService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
