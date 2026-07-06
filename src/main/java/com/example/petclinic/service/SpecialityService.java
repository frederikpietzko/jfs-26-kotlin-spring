package com.example.petclinic.service;

import com.example.petclinic.api.dto.SpecialityResponse;
import com.example.petclinic.domain.entity.Speciality;
import com.example.petclinic.domain.repository.SpecialityRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class SpecialityService {

    private final SpecialityRepository specialityRepository;

    public SpecialityService(SpecialityRepository specialityRepository) {
        this.specialityRepository = specialityRepository;
    }

    @Transactional(readOnly = true)
    public List<SpecialityResponse> findAll() {
        return specialityRepository.findAll().stream()
                .map(SpecialityResponse::fromEntity)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public SpecialityResponse findById(Long id) {
        return specialityRepository.findById(id)
                .map(SpecialityResponse::fromEntity)
                .orElse(null);
    }

    @Transactional
    public SpecialityResponse save(Speciality entity) {
        Speciality saved = specialityRepository.save(entity);
        return SpecialityResponse.fromEntity(saved);
    }

    @Transactional
    public SpecialityResponse update(Long id, Speciality entity) {
        if (!specialityRepository.existsById(id)) {
            return null;
        }
        entity.setId(id);
        Speciality saved = specialityRepository.save(entity);
        return SpecialityResponse.fromEntity(saved);
    }

    @Transactional
    public void delete(Long id) {
        specialityRepository.deleteById(id);
    }
}
