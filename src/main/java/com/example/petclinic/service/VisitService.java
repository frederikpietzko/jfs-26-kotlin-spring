package com.example.petclinic.service;

import com.example.petclinic.domain.entity.Pet;
import com.example.petclinic.domain.entity.Visit;
import com.example.petclinic.domain.repository.PetRepository;
import com.example.petclinic.domain.repository.VisitRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class VisitService {

    private final VisitRepository visitRepository;
    private final PetRepository petRepository;

    @Transactional(readOnly = true)
    public List<Visit> findAll() {
        log.debug("Fetching all visits");
        List<Visit> visits = visitRepository.findAll();
        log.debug("Found {} visits", visits.size());
        return visits;
    }

    @Transactional(readOnly = true)
    public List<Visit> findByPetId(Long petId) {
        log.debug("Fetching visits for pet id: {}", petId);
        final var pet = petRepository.findById(petId).orElse(null);
        if (pet == null) {
            log.warn("Pet not found with id: {}", petId);
            return List.of();
        }
        List<Visit> visits = visitRepository.findAllByPet(pet);
        log.debug("Found {} visits for pet id: {}", visits.size(), petId);
        return visits;
    }

    @Transactional(readOnly = true)
    public Visit findById(Long id) {
        log.debug("Fetching visit with id: {}", id);
        Visit visit = visitRepository.findById(id).orElse(null);
        if (visit == null) {
            log.warn("Visit not found with id: {}", id);
        }
        return visit;
    }

    @Transactional
    public Visit save(Visit entity) {
        log.debug("Saving visit for pet id: {}", entity.getPet().getId());
        final var pet = petRepository.findById(entity.getPet().getId()).orElse(null);
        if (pet == null) {
            log.warn("Cannot save visit - pet not found with id: {}", entity.getPet().getId());
            return null;
        }
        final var withPet = new Visit(entity.getId(), entity.getDate(), entity.getDescription(), pet);
        Visit savedVisit = visitRepository.save(withPet);
        log.info("Visit saved successfully with id: {}", savedVisit.getId());
        return savedVisit;
    }

    @Transactional
    public Visit update(Long id, Visit entity) {
        log.debug("Updating visit with id: {}", id);
        final var existingOpt = visitRepository.findById(id);
        if (existingOpt.isEmpty()) {
            log.warn("Visit not found for update with id: {}", id);
            return null;
        }
        final var pet = petRepository.findById(entity.getPet().getId()).orElse(null);
        if (pet == null) {
            log.warn("Cannot update visit - pet not found with id: {}", entity.getPet().getId());
            return null;
        }
        final var existing = existingOpt.get();
        final var updated = new Visit(existing.getId(), entity.getDate(), entity.getDescription(), pet);
        Visit updatedVisit = visitRepository.save(updated);
        log.info("Visit updated successfully with id: {}", updatedVisit.getId());
        return updatedVisit;
    }

    @Transactional
    public void delete(Long id) {
        log.debug("Deleting visit with id: {}", id);
        visitRepository.deleteById(id);
        log.info("Visit deleted successfully with id: {}", id);
    }
}
