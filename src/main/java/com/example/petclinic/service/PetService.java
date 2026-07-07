package com.example.petclinic.service;

import com.example.petclinic.domain.entity.Owner;
import com.example.petclinic.domain.entity.Pet;
import com.example.petclinic.domain.repository.OwnerRepository;
import com.example.petclinic.domain.repository.PetRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class PetService {

    private final PetRepository petRepository;
    private final OwnerRepository ownerRepository;

    @Transactional(readOnly = true)
    public List<Pet> findByOwnerId(Long ownerId) {
        log.debug("Fetching pets for owner id: {}", ownerId);
        final var owner = ownerRepository.findById(ownerId).orElse(null);
        if (owner == null) {
            log.warn("Owner not found with id: {}", ownerId);
            return List.of();
        }
        List<Pet> pets = petRepository.findAllByOwner(owner);
        log.debug("Found {} pets for owner id: {}", pets.size(), ownerId);
        return pets;
    }

    @Transactional(readOnly = true)
    public Pet findById(Long id) {
        log.debug("Fetching pet with id: {}", id);
        Pet pet = petRepository.findById(id).orElse(null);
        if (pet == null) {
            log.warn("Pet not found with id: {}", id);
        }
        return pet;
    }

    @Transactional
    public Pet save(Pet entity) {
        log.debug("Saving pet: {} for owner id: {}", entity.getName(), entity.getOwner().getId());
        final var owner = ownerRepository.findById(entity.getOwner().getId()).orElse(null);
        if (owner == null) {
            log.warn("Cannot save pet - owner not found with id: {}", entity.getOwner().getId());
            return null;
        }
        entity.setOwner(owner);
        Pet savedPet = petRepository.save(entity);
        log.info("Pet saved successfully with id: {}", savedPet.getId());
        return savedPet;
    }

    @Transactional
    public Pet update(Long id, Pet entity) {
        log.debug("Updating pet with id: {}", id);
        final var existingOpt = petRepository.findById(id);
        if (existingOpt.isEmpty()) {
            log.warn("Pet not found for update with id: {}", id);
            return null;
        }
        final var owner = ownerRepository.findById(entity.getOwner().getId()).orElse(null);
        if (owner == null) {
            log.warn("Cannot update pet - owner not found with id: {}", entity.getOwner().getId());
            return null;
        }
        final var existing = existingOpt.get();
        existing.setName(entity.getName());
        existing.setType(entity.getType());
        existing.setBirthDate(entity.getBirthDate());
        existing.setOwner(owner);
        Pet updatedPet = petRepository.save(existing);
        log.info("Pet updated successfully with id: {}", updatedPet.getId());
        return updatedPet;
    }

    @Transactional
    public void delete(Long id) {
        log.debug("Deleting pet with id: {}", id);
        petRepository.deleteById(id);
        log.info("Pet deleted successfully with id: {}", id);
    }
}
