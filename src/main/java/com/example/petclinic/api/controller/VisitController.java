package com.example.petclinic.api.controller;

import com.example.petclinic.api.dto.VisitRequest;
import com.example.petclinic.api.dto.VisitResponse;
import com.example.petclinic.api.mapper.VisitMapper;
import com.example.petclinic.domain.entity.Pet;
import com.example.petclinic.domain.entity.Visit;
import com.example.petclinic.service.VisitService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashSet;
import java.util.List;

@RestController
@RequestMapping("/owners/{ownerId}/pets/{petId}/visits")
@RequiredArgsConstructor
public class VisitController {

    private final VisitService visitService;
    private final VisitMapper visitMapper;

    @GetMapping
    public List<VisitResponse> getByPetId(@PathVariable Long petId) {
        return visitService.findByPetId(petId).stream()
                .map(visitMapper::toVisitResponse)
                .toList();
    }

    @GetMapping("/{id}")
    public ResponseEntity<VisitResponse> getById(@PathVariable Long id) {
        final var visit = visitService.findById(id);
        if (visit == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(visitMapper.toVisitResponse(visit));
    }

    @PostMapping
    public ResponseEntity<VisitResponse> create(@PathVariable Long petId, @Valid @RequestBody VisitRequest request) {
        final var pet = new Pet(petId, null, null, null, null, new HashSet<>());

        final var entity = visitMapper.toVisit(request);
        entity.setPet(pet);

        final var saved = visitService.save(entity);
        return ResponseEntity.status(HttpStatus.CREATED).body(visitMapper.toVisitResponse(saved));
    }

    @PutMapping("/{id}")
    public ResponseEntity<VisitResponse> update(@PathVariable Long petId, @PathVariable Long id, @Valid @RequestBody VisitRequest request) {
        final var pet = new Pet(petId, null, null, null, null, new HashSet<>());

        final var entity = visitMapper.toVisit(request);
        entity.setId(id);
        entity.setPet(pet);

        final var updated = visitService.update(id, entity);
        if (updated == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(visitMapper.toVisitResponse(updated));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        visitService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
