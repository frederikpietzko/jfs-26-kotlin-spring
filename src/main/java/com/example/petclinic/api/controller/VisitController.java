package com.example.petclinic.api.controller;

import com.example.petclinic.api.dto.VisitRequest;
import com.example.petclinic.api.dto.VisitResponse;
import com.example.petclinic.domain.entity.Pet;
import com.example.petclinic.domain.entity.Visit;
import com.example.petclinic.service.PetService;
import com.example.petclinic.service.VisitService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/owners/{ownerId}/pets/{petId}/visits")
public class VisitController {

    private final VisitService visitService;
    private final PetService petService;

    public VisitController(VisitService visitService, PetService petService) {
        this.visitService = visitService;
        this.petService = petService;
    }

    @GetMapping
    public List<VisitResponse> getByPetId(@PathVariable Long petId) {
        return visitService.findByPetId(petId);
    }

    @GetMapping("/{id}")
    public ResponseEntity<VisitResponse> getById(@PathVariable Long id) {
        VisitResponse visit = visitService.findById(id);
        if (visit == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(visit);
    }

    @PostMapping
    public ResponseEntity<VisitResponse> create(@PathVariable Long petId, @Valid @RequestBody VisitRequest request) {
        Pet pet = new Pet();
        pet.setId(petId);

        Visit entity = new Visit();
        entity.setDate(request.getDate());
        entity.setDescription(request.getDescription());
        entity.setPet(pet);

        VisitResponse saved = visitService.save(entity);
        return ResponseEntity.status(HttpStatus.CREATED).body(saved);
    }

    @PutMapping("/{id}")
    public ResponseEntity<VisitResponse> update(@PathVariable Long petId, @PathVariable Long id, @Valid @RequestBody VisitRequest request) {
        Pet pet = new Pet();
        pet.setId(petId);

        Visit entity = new Visit();
        entity.setId(id);
        entity.setDate(request.getDate());
        entity.setDescription(request.getDescription());
        entity.setPet(pet);

        VisitResponse updated = visitService.update(id, entity);
        if (updated == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(updated);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        visitService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
