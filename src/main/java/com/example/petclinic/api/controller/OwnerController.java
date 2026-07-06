package com.example.petclinic.api.controller;

import com.example.petclinic.api.dto.OwnerRequest;
import com.example.petclinic.api.dto.OwnerResponse;
import com.example.petclinic.domain.entity.Owner;
import com.example.petclinic.service.OwnerService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/owners")
public class OwnerController {

    private final OwnerService ownerService;

    public OwnerController(OwnerService ownerService) {
        this.ownerService = ownerService;
    }

    @GetMapping
    public List<OwnerResponse> getAll() {
        return ownerService.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<OwnerResponse> getById(@PathVariable Long id) {
        OwnerResponse owner = ownerService.findById(id);
        if (owner == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(owner);
    }

    @PostMapping
    public ResponseEntity<OwnerResponse> create(@Valid @RequestBody OwnerRequest request) {
        Owner entity = new Owner();
        entity.setFirstName(request.getFirstName());
        entity.setLastName(request.getLastName());
        entity.setAddress(request.getAddress());
        entity.setCity(request.getCity());
        entity.setTelephone(request.getTelephone());
        
        OwnerResponse saved = ownerService.save(entity);
        return ResponseEntity.status(HttpStatus.CREATED).body(saved);
    }

    @PutMapping("/{id}")
    public ResponseEntity<OwnerResponse> update(@PathVariable Long id, @Valid @RequestBody OwnerRequest request) {
        Owner entity = new Owner();
        entity.setFirstName(request.getFirstName());
        entity.setLastName(request.getLastName());
        entity.setAddress(request.getAddress());
        entity.setCity(request.getCity());
        entity.setTelephone(request.getTelephone());
        
        OwnerResponse updated = ownerService.update(id, entity);
        if (updated == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(updated);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        ownerService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
