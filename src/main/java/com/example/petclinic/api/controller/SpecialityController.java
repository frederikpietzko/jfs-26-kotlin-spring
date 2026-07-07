package com.example.petclinic.api.controller;

import com.example.petclinic.api.dto.SpecialityRequest;
import com.example.petclinic.api.dto.SpecialityResponse;
import com.example.petclinic.api.mapper.SpecialityMapper;
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
    private final SpecialityMapper specialityMapper;

    @GetMapping
    public List<SpecialityResponse> getAll() {
        return specialityService.findAll().stream()
                .map(specialityMapper::toSpecialityResponse)
                .toList();
    }

    @GetMapping("/{id}")
    public ResponseEntity<SpecialityResponse> getById(@PathVariable Long id) {
        final var speciality = specialityService.findById(id);
        if (speciality == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(specialityMapper.toSpecialityResponse(speciality));
    }

    @PostMapping
    public ResponseEntity<SpecialityResponse> create(@Valid @RequestBody SpecialityRequest request) {
        final var entity = specialityMapper.toSpeciality(request);
        entity.setVets(new HashSet<>());

        final var saved = specialityService.save(entity);
        return ResponseEntity.status(HttpStatus.CREATED).body(specialityMapper.toSpecialityResponse(saved));
    }

    @PutMapping("/{id}")
    public ResponseEntity<SpecialityResponse> update(@PathVariable Long id, @Valid @RequestBody SpecialityRequest request) {
        final var entity = specialityMapper.toSpeciality(request);
        entity.setVets(new HashSet<>());

        final var updated = specialityService.update(id, entity);
        if (updated == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(specialityMapper.toSpecialityResponse(updated));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        specialityService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
