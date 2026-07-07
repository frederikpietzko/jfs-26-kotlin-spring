package com.example.petclinic.service;

import com.example.petclinic.domain.entity.Speciality;
import com.example.petclinic.domain.entity.Vet;
import com.example.petclinic.domain.repository.SpecialityRepository;
import com.example.petclinic.domain.repository.VetRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;


@Service
@RequiredArgsConstructor
public class VetService {

    private final VetRepository vetRepository;
    private final SpecialityRepository specialityRepository;

    @Transactional(readOnly = true)
    public List<Vet> findAll() {
        return vetRepository.findAll();
    }

    @Transactional(readOnly = true)
    public Vet findById(Long id) {
        return vetRepository.findById(id)
                .orElse(null);
    }

    @Transactional
    public Vet save(Vet entity) {
        if (entity.getSpecialties() != null) {
            for (Speciality specialty : entity.getSpecialties()) {
                final var managed = specialityRepository.findById(specialty.getId()).orElse(specialty);
                specialty.setId(managed.getId());
            }
        }
        return vetRepository.save(entity);
    }

    @Transactional
    public Vet update(Long id, Vet entity) {
        final var existingOpt = vetRepository.findById(id);
        if (existingOpt.isEmpty()) {
            return null;
        }
        if (entity.getSpecialties() != null) {
            for (Speciality specialty : entity.getSpecialties()) {
                final var managed = specialityRepository.findById(specialty.getId()).orElse(specialty);
                specialty.setId(managed.getId());
            }
        }
        final var existing = existingOpt.get();
        existing.setFirstName(entity.getFirstName());
        existing.setLastName(entity.getLastName());
        existing.setSpecialties(entity.getSpecialties());
        return vetRepository.save(existing);
    }

    @Transactional
    public void delete(Long id) {
        vetRepository.deleteById(id);
    }
}
