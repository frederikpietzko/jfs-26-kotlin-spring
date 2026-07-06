package com.example.petclinic.api.controller;

import com.example.petclinic.api.dto.VetRequest;
import com.example.petclinic.api.dto.VetResponse;
import com.example.petclinic.domain.entity.Speciality;
import com.example.petclinic.domain.entity.Vet;
import com.example.petclinic.service.SpecialityService;
import com.example.petclinic.service.VetService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import java.util.List;
import java.util.Set;

@RestController
@RequestMapping("/vets")
public class VetController {

    private final VetService vetService;
    private final SpecialityService specialityService;

    public VetController(VetService vetService, SpecialityService specialityService) {
        this.vetService = vetService;
        this.specialityService = specialityService;
    }

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
        Set<Speciality> specialties = request.getSpecialityIds() != null 
                ? request.getSpecialityIds().stream()
                        .map(id -> {
                            Speciality s = new Speciality();
                            s.setId(id);
                            return s;
                        })
                        .collect(java.util.stream.Collectors.toSet())
                : Set.of();

        Vet entity = new Vet();
        entity.setFirstName(request.getFirstName());
        entity.setLastName(request.getLastName());
        entity.setSpecialties(specialties);

        VetResponse saved = vetService.save(entity);
        return ResponseEntity.status(HttpStatus.CREATED).body(saved);
    }

    @PutMapping("/{id}")
    public ResponseEntity<VetResponse> update(@PathVariable Long id, @Valid @RequestBody VetRequest request) {
        Set<Speciality> specialties = request.getSpecialityIds() != null 
                ? request.getSpecialityIds().stream()
                        .map(sId -> {
                            Speciality s = new Speciality();
                            s.setId(sId);
                            return s;
                        })
                        .collect(java.util.stream.Collectors.toSet())
                : Set.of();

        Vet entity = new Vet();
        entity.setFirstName(request.getFirstName());
        entity.setLastName(request.getLastName());
        entity.setSpecialties(specialties);

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
