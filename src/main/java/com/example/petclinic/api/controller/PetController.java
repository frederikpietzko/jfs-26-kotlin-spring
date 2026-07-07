package com.example.petclinic.api.controller;

import com.example.petclinic.api.dto.PetRequest;
import com.example.petclinic.api.dto.PetResponse;
import com.example.petclinic.domain.entity.Owner;
import com.example.petclinic.domain.entity.Pet;
import com.example.petclinic.service.PetService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashSet;
import java.util.List;

@RestController
@RequestMapping("/owners/{ownerId}/pets")
@RequiredArgsConstructor
public class PetController {

    private final PetService petService;

    @GetMapping
    public List<PetResponse> getByOwnerId(@PathVariable Long ownerId) {
        return petService.findByOwnerId(ownerId).stream()
                .map(PetResponse::fromEntity)
                .toList();
    }

    @GetMapping("/{id}")
    public ResponseEntity<PetResponse> getById(@PathVariable Long id) {
        final var pet = petService.findById(id);
        if (pet == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(PetResponse.fromEntity(pet));
    }

    @PostMapping
    public ResponseEntity<PetResponse> create(@PathVariable Long ownerId, @Valid @RequestBody PetRequest request) {
        final var owner = new Owner(ownerId, null, null, null, null, null, new HashSet<>());

        final var entity = new Pet(null, request.name(), request.type().toEntity(), null, owner, new HashSet<>());

        final var saved = petService.save(entity);
        return ResponseEntity.status(HttpStatus.CREATED).body(PetResponse.fromEntity(saved));
    }

    @PutMapping("/{id}")
    public ResponseEntity<PetResponse> update(@PathVariable Long ownerId, @PathVariable Long id, @Valid @RequestBody PetRequest request) {
        final var owner = new Owner(ownerId, null, null, null, null, null, new HashSet<>());

        final var entity = new Pet(id, request.name(), request.type().toEntity(), null, owner, new HashSet<>());

        final var updated = petService.update(id, entity);
        if (updated == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(PetResponse.fromEntity(updated));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        petService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
