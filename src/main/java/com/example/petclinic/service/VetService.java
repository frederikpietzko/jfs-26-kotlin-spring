package com.example.petclinic.service;

import com.example.petclinic.domain.entity.Speciality;
import com.example.petclinic.domain.entity.Vet;
import com.example.petclinic.domain.repository.SpecialityRepository;
import com.example.petclinic.domain.repository.VetRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class VetService {

    private final VetRepository vetRepository;
    private final SpecialityRepository specialityRepository;

    @Transactional(readOnly = true)
    public List<Vet> findAll() {
        log.debug("Fetching all vets");
        List<Vet> vets = vetRepository.findAll();
        log.debug("Found {} vets", vets.size());
        return vets;
    }

    @Transactional(readOnly = true)
    public Vet findById(Long id) {
        log.debug("Fetching vet with id: {}", id);
        Vet vet = vetRepository.findById(id).orElse(null);
        if (vet == null) {
            log.warn("Vet not found with id: {}", id);
        }
        return vet;
    }

    @Transactional
    public Vet save(Vet entity) {
        log.debug("Saving vet: {} {}", entity.getFirstName(), entity.getLastName());
        if (entity.getSpecialties() != null) {
            for (Speciality specialty : entity.getSpecialties()) {
                final var managed = specialityRepository.findById(specialty.getId()).orElse(specialty);
                specialty.setId(managed.getId());
            }
            log.debug("Vet has {} specialties", entity.getSpecialties().size());
        }
        Vet savedVet = vetRepository.save(entity);
        log.info("Vet saved successfully with id: {}", savedVet.getId());
        return savedVet;
    }

    @Transactional
    public Vet update(Long id, Vet entity) {
        log.debug("Updating vet with id: {}", id);
        final var existingOpt = vetRepository.findById(id);
        if (existingOpt.isEmpty()) {
            log.warn("Vet not found for update with id: {}", id);
            return null;
        }
        if (entity.getSpecialties() != null) {
            for (Speciality specialty : entity.getSpecialties()) {
                final var managed = specialityRepository.findById(specialty.getId()).orElse(specialty);
                specialty.setId(managed.getId());
            }
            log.debug("Updating vet with {} specialties", entity.getSpecialties().size());
        }
        final var existing = existingOpt.get();
        existing.setFirstName(entity.getFirstName());
        existing.setLastName(entity.getLastName());
        existing.setSpecialties(entity.getSpecialties());
        Vet updatedVet = vetRepository.save(existing);
        log.info("Vet updated successfully with id: {}", updatedVet.getId());
        return updatedVet;
    }

    @Transactional
    public void delete(Long id) {
        log.debug("Deleting vet with id: {}", id);
        vetRepository.deleteById(id);
        log.info("Vet deleted successfully with id: {}", id);
    }
}
