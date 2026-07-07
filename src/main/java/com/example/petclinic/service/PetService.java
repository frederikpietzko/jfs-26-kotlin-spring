package com.example.petclinic.service;

import com.example.petclinic.api.dto.PetResponse;
import com.example.petclinic.domain.entity.Owner;
import com.example.petclinic.domain.entity.Pet;
import com.example.petclinic.domain.repository.OwnerRepository;
import com.example.petclinic.domain.repository.PetRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PetService {

    private final PetRepository petRepository;
    private final OwnerRepository ownerRepository;

    @Transactional(readOnly = true)
    public List<PetResponse> findByOwnerId(Long ownerId) {
        Owner owner = ownerRepository.findById(ownerId).orElse(null);
        if (owner == null) {
            return List.of();
        }
        return petRepository.findAllByOwner(owner).stream()
                .map(PetResponse::fromEntity)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public PetResponse findById(Long id) {
        return petRepository.findById(id)
                .map(PetResponse::fromEntity)
                .orElse(null);
    }

    @Transactional
    public PetResponse save(Pet entity) {
        Owner owner = ownerRepository.findById(entity.getOwner().getId()).orElse(null);
        if (owner == null) {
            return null;
        }
        entity.setOwner(owner);
        Pet saved = petRepository.save(entity);
        return PetResponse.fromEntity(saved);
    }

    @Transactional
    public PetResponse update(Long id, Pet entity) {
        Optional<Pet> existingOpt = petRepository.findById(id);
        if (existingOpt.isEmpty()) {
            return null;
        }
        Owner owner = ownerRepository.findById(entity.getOwner().getId()).orElse(null);
        if (owner == null) {
            return null;
        }
        Pet existing = existingOpt.get();
        existing.setName(entity.getName());
        existing.setType(entity.getType());
        existing.setBirthDate(entity.getBirthDate());
        existing.setOwner(owner);
        Pet saved = petRepository.save(existing);
        return PetResponse.fromEntity(saved);
    }

    @Transactional
    public void delete(Long id) {
        petRepository.deleteById(id);
    }
}
