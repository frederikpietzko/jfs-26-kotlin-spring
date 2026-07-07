package com.example.petclinic.service;

import com.example.petclinic.domain.entity.Owner;
import com.example.petclinic.domain.entity.Pet;
import com.example.petclinic.domain.repository.OwnerRepository;
import com.example.petclinic.domain.repository.PetRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;


@Service
@RequiredArgsConstructor
public class PetService {

    private final PetRepository petRepository;
    private final OwnerRepository ownerRepository;

    @Transactional(readOnly = true)
    public List<Pet> findByOwnerId(Long ownerId) {
        final var owner = ownerRepository.findById(ownerId).orElse(null);
        if (owner == null) {
            return List.of();
        }
        return petRepository.findAllByOwner(owner);
    }

    @Transactional(readOnly = true)
    public Pet findById(Long id) {
        return petRepository.findById(id)
                .orElse(null);
    }

    @Transactional
    public Pet save(Pet entity) {
        final var owner = ownerRepository.findById(entity.getOwner().getId()).orElse(null);
        if (owner == null) {
            return null;
        }
        entity.setOwner(owner);
        return petRepository.save(entity);
    }

    @Transactional
    public Pet update(Long id, Pet entity) {
        final var existingOpt = petRepository.findById(id);
        if (existingOpt.isEmpty()) {
            return null;
        }
        final var owner = ownerRepository.findById(entity.getOwner().getId()).orElse(null);
        if (owner == null) {
            return null;
        }
        final var existing = existingOpt.get();
        existing.setName(entity.getName());
        existing.setType(entity.getType());
        existing.setBirthDate(entity.getBirthDate());
        existing.setOwner(owner);
        return petRepository.save(existing);
    }

    @Transactional
    public void delete(Long id) {
        petRepository.deleteById(id);
    }
}
