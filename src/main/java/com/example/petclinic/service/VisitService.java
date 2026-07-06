package com.example.petclinic.service;

import com.example.petclinic.api.dto.VisitResponse;
import com.example.petclinic.domain.entity.Owner;
import com.example.petclinic.domain.entity.Pet;
import com.example.petclinic.domain.entity.Visit;
import com.example.petclinic.domain.repository.OwnerRepository;
import com.example.petclinic.domain.repository.PetRepository;
import com.example.petclinic.domain.repository.VisitRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class VisitService {

    private final VisitRepository visitRepository;
    private final PetRepository petRepository;
    private final OwnerRepository ownerRepository;

    public VisitService(VisitRepository visitRepository, PetRepository petRepository, OwnerRepository ownerRepository) {
        this.visitRepository = visitRepository;
        this.petRepository = petRepository;
        this.ownerRepository = ownerRepository;
    }

    @Transactional(readOnly = true)
    public List<VisitResponse> findByPetId(Long petId) {
        Pet pet = petRepository.findById(petId).orElse(null);
        if (pet == null) {
            return List.of();
        }
        return visitRepository.findAllByPet(pet).stream()
                .map(VisitResponse::fromEntity)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public VisitResponse findById(Long id) {
        return visitRepository.findById(id)
                .map(VisitResponse::fromEntity)
                .orElse(null);
    }

    @Transactional
    public VisitResponse save(Visit entity) {
        Pet pet = petRepository.findById(entity.getPet().getId()).orElse(null);
        if (pet == null) {
            return null;
        }
        entity.setPet(pet);
        Visit saved = visitRepository.save(entity);
        return VisitResponse.fromEntity(saved);
    }

    @Transactional
    public VisitResponse update(Long id, Visit entity) {
        Optional<Visit> existingOpt = visitRepository.findById(id);
        if (existingOpt.isEmpty()) {
            return null;
        }
        Pet pet = petRepository.findById(entity.getPet().getId()).orElse(null);
        if (pet == null) {
            return null;
        }
        Visit existing = existingOpt.get();
        existing.setDate(entity.getDate());
        existing.setDescription(entity.getDescription());
        existing.setPet(pet);
        Visit saved = visitRepository.save(existing);
        return VisitResponse.fromEntity(saved);
    }

    @Transactional
    public void delete(Long id) {
        visitRepository.deleteById(id);
    }
}
