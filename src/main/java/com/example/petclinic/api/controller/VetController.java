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
import java.util.Set;

@RestController
@RequestMapping("/vets")
@RequiredArgsConstructor
public class VetController {

    private final VetService vetService;

    @GetMapping
    public List<VetResponse> getAll() {
        return vetService.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<VetResponse> getById(@PathVariable Long id) {
        VetResponse vet = vetService.findById(id);
        if (vet == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(vet);
    }

    @PostMapping
    public ResponseEntity<VetResponse> create(@Valid @RequestBody VetRequest request) {
        Set<Speciality> specialties = request.specialityIds() != null
                ? request.specialityIds().stream()
                .map(id -> new Speciality(id, null, new java.util.HashSet<>()))
                .collect(java.util.stream.Collectors.toSet())
                : Set.of();

        Vet entity = new Vet(null, request.firstName(), request.lastName(), specialties);

        VetResponse saved = vetService.save(entity);
        return ResponseEntity.status(HttpStatus.CREATED).body(saved);
    }

    @PutMapping("/{id}")
    public ResponseEntity<VetResponse> update(@PathVariable Long id, @Valid @RequestBody VetRequest request) {
        Set<Speciality> specialties = request.specialityIds() != null
                ? request.specialityIds().stream()
                .map(sId -> new Speciality(sId, null, new java.util.HashSet<>()))
                .collect(java.util.stream.Collectors.toSet())
                : Set.of();

        Vet entity = new Vet(null, request.firstName(), request.lastName(), specialties);

        VetResponse updated = vetService.update(id, entity);
        if (updated == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(updated);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        vetService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
