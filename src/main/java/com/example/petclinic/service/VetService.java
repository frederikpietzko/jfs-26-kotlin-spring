package com.example.petclinic.service;

import com.example.petclinic.api.dto.VetResponse;
import com.example.petclinic.domain.entity.Speciality;
import com.example.petclinic.domain.entity.Vet;
import com.example.petclinic.domain.repository.SpecialityRepository;
import com.example.petclinic.domain.repository.VetRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class VetService {

    private final VetRepository vetRepository;
    private final SpecialityRepository specialityRepository;

    public VetService(VetRepository vetRepository, SpecialityRepository specialityRepository) {
        this.vetRepository = vetRepository;
        this.specialityRepository = specialityRepository;
    }

    @Transactional(readOnly = true)
    public List<VetResponse> findAll() {
        return vetRepository.findAll().stream()
                .map(VetResponse::fromEntity)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public VetResponse findById(Long id) {
        return vetRepository.findById(id)
                .map(VetResponse::fromEntity)
                .orElse(null);
    }

    @Transactional
    public VetResponse save(Vet entity) {
        if (entity.getSpecialties() != null) {
            for (Speciality specialty : entity.getSpecialties()) {
                Speciality managed = specialityRepository.findById(specialty.getId()).orElse(specialty);
                specialty.setId(managed.getId());
            }
        }
        Vet saved = vetRepository.save(entity);
        return VetResponse.fromEntity(saved);
    }

    @Transactional
    public VetResponse update(Long id, Vet entity) {
        Optional<Vet> existingOpt = vetRepository.findById(id);
        if (existingOpt.isEmpty()) {
            return null;
        }
        if (entity.getSpecialties() != null) {
            for (Speciality specialty : entity.getSpecialties()) {
                Speciality managed = specialityRepository.findById(specialty.getId()).orElse(specialty);
                specialty.setId(managed.getId());
            }
        }
        Vet existing = existingOpt.get();
        existing.setFirstName(entity.getFirstName());
        existing.setLastName(entity.getLastName());
        existing.setSpecialties(entity.getSpecialties());
        Vet saved = vetRepository.save(existing);
        return VetResponse.fromEntity(saved);
    }

    @Transactional
    public void delete(Long id) {
        vetRepository.deleteById(id);
    }
}
