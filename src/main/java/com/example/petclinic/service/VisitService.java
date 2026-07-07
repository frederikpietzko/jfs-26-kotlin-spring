package com.example.petclinic.service;

import com.example.petclinic.domain.entity.Pet;
import com.example.petclinic.domain.entity.Visit;
import com.example.petclinic.domain.repository.PetRepository;
import com.example.petclinic.domain.repository.VisitRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;


@Service
@RequiredArgsConstructor
public class VisitService {

    private final VisitRepository visitRepository;
    private final PetRepository petRepository;

    @Transactional(readOnly = true)
    public List<Visit> findByPetId(Long petId) {
        final var pet = petRepository.findById(petId).orElse(null);
        if (pet == null) {
            return List.of();
        }
        return visitRepository.findAllByPet(pet);
    }

    @Transactional(readOnly = true)
    public Visit findById(Long id) {
        return visitRepository.findById(id)
                .orElse(null);
    }

    @Transactional
    public Visit save(Visit entity) {
        final var pet = petRepository.findById(entity.getPet().getId()).orElse(null);
        if (pet == null) {
            return null;
        }
        final var withPet = new Visit(entity.getId(), entity.getDate(), entity.getDescription(), pet);
        return visitRepository.save(withPet);
    }

    @Transactional
    public Visit update(Long id, Visit entity) {
        final var existingOpt = visitRepository.findById(id);
        if (existingOpt.isEmpty()) {
            return null;
        }
        final var pet = petRepository.findById(entity.getPet().getId()).orElse(null);
        if (pet == null) {
            return null;
        }
        final var existing = existingOpt.get();
        final var updated = new Visit(existing.getId(), entity.getDate(), entity.getDescription(), pet);
        return visitRepository.save(updated);
    }

    @Transactional
    public void delete(Long id) {
        visitRepository.deleteById(id);
    }
}
