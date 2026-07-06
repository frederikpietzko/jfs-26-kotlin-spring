package com.example.petclinic.api.controller;

import com.example.petclinic.api.dto.SpecialityRequest;
import com.example.petclinic.api.dto.SpecialityResponse;
import com.example.petclinic.domain.entity.Speciality;
import com.example.petclinic.service.SpecialityService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/specialities")
public class SpecialityController {

    private final SpecialityService specialityService;

    public SpecialityController(SpecialityService specialityService) {
        this.specialityService = specialityService;
    }

    @GetMapping
    public List<SpecialityResponse> getAll() {
        return specialityService.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<SpecialityResponse> getById(@PathVariable Long id) {
        SpecialityResponse speciality = specialityService.findById(id);
        if (speciality == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(speciality);
    }

    @PostMapping
    public ResponseEntity<SpecialityResponse> create(@Valid @RequestBody SpecialityRequest request) {
        Speciality entity = new Speciality();
        entity.setName(request.getName());

        SpecialityResponse saved = specialityService.save(entity);
        return ResponseEntity.status(HttpStatus.CREATED).body(saved);
    }

    @PutMapping("/{id}")
    public ResponseEntity<SpecialityResponse> update(@PathVariable Long id, @Valid @RequestBody SpecialityRequest request) {
        Speciality entity = new Speciality();
        entity.setName(request.getName());

        SpecialityResponse updated = specialityService.update(id, entity);
        if (updated == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(updated);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        specialityService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
