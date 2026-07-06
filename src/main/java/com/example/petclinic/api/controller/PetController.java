package com.example.petclinic.api.controller;

import com.example.petclinic.api.dto.PetRequest;
import com.example.petclinic.api.dto.PetResponse;
import com.example.petclinic.domain.entity.Owner;
import com.example.petclinic.domain.entity.Pet;
import com.example.petclinic.service.OwnerService;
import com.example.petclinic.service.PetService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/owners/{ownerId}/pets")
public class PetController {

    private final PetService petService;
    private final OwnerService ownerService;

    public PetController(PetService petService, OwnerService ownerService) {
        this.petService = petService;
        this.ownerService = ownerService;
    }

    @GetMapping
    public List<PetResponse> getByOwnerId(@PathVariable Long ownerId) {
        return petService.findByOwnerId(ownerId);
    }

    @GetMapping("/{id}")
    public ResponseEntity<PetResponse> getById(@PathVariable Long id) {
        PetResponse pet = petService.findById(id);
        if (pet == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(pet);
    }

    @PostMapping
    public ResponseEntity<PetResponse> create(@PathVariable Long ownerId, @Valid @RequestBody PetRequest request) {
        Owner owner = new Owner();
        owner.setId(ownerId);
        
        Pet entity = new Pet();
        entity.setName(request.getName());
        entity.setType(request.getType().toEntity());
        entity.setOwner(owner);

        PetResponse saved = petService.save(entity);
        return ResponseEntity.status(HttpStatus.CREATED).body(saved);
    }

    @PutMapping("/{id}")
    public ResponseEntity<PetResponse> update(@PathVariable Long ownerId, @PathVariable Long id, @Valid @RequestBody PetRequest request) {
        Owner owner = new Owner();
        owner.setId(ownerId);

        Pet entity = new Pet();
        entity.setId(id);
        entity.setName(request.getName());
        entity.setType(request.getType().toEntity());
        entity.setOwner(owner);

        PetResponse updated = petService.update(id, entity);
        if (updated == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(updated);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        petService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
