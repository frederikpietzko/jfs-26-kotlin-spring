package com.example.petclinic.api.controller;

import com.example.petclinic.api.dto.VetRequest;
import com.example.petclinic.api.dto.VetResponse;
import com.example.petclinic.api.mapper.VetMapper;
import com.example.petclinic.domain.entity.Vet;
import com.example.petclinic.service.VetService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/vets")
@RequiredArgsConstructor
public class VetController {

    private final VetService vetService;
    private final VetMapper vetMapper;

    @GetMapping
    public List<VetResponse> getAll() {
        return vetService.findAll().stream()
                .map(vetMapper::toVetResponse)
                .toList();
    }

    @GetMapping("/{id}")
    public ResponseEntity<VetResponse> getById(@PathVariable Long id) {
        final var vet = vetService.findById(id);
        if (vet == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(vetMapper.toVetResponse(vet));
    }

    @PostMapping
    public ResponseEntity<VetResponse> create(@Valid @RequestBody VetRequest request) {
        final var entity = vetMapper.toVet(request);

        final var saved = vetService.save(entity);
        return ResponseEntity.status(HttpStatus.CREATED).body(vetMapper.toVetResponse(saved));
    }

    @PutMapping("/{id}")
    public ResponseEntity<VetResponse> update(@PathVariable Long id, @Valid @RequestBody VetRequest request) {
        final var entity = vetMapper.toVet(request);

        final var updated = vetService.update(id, entity);
        if (updated == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(vetMapper.toVetResponse(updated));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        vetService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
