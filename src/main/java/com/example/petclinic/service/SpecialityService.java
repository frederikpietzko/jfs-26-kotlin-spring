package com.example.petclinic.service;

import com.example.petclinic.domain.entity.Speciality;
import com.example.petclinic.domain.repository.SpecialityRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;


@Service
@RequiredArgsConstructor
public class SpecialityService {

    private final SpecialityRepository specialityRepository;

    @Transactional(readOnly = true)
    public List<Speciality> findAll() {
        return specialityRepository.findAll();
    }

    @Transactional(readOnly = true)
    public Speciality findById(Long id) {
        return specialityRepository.findById(id)
                .orElse(null);
    }

    @Transactional
    public Speciality save(Speciality entity) {
        return specialityRepository.save(entity);
    }

    @Transactional
    public Speciality update(Long id, Speciality entity) {
        if (!specialityRepository.existsById(id)) {
            return null;
        }
        entity.setId(id);
        return specialityRepository.save(entity);
    }

    @Transactional
    public void delete(Long id) {
        specialityRepository.deleteById(id);
    }
}
