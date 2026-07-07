package com.example.petclinic.service;

import com.example.petclinic.domain.entity.Speciality;
import com.example.petclinic.domain.repository.SpecialityRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class SpecialityService {

    private final SpecialityRepository specialityRepository;

    @Transactional(readOnly = true)
    public List<Speciality> findAll() {
        log.debug("Fetching all specialities");
        List<Speciality> specialities = specialityRepository.findAll();
        log.debug("Found {} specialities", specialities.size());
        return specialities;
    }

    @Transactional(readOnly = true)
    public Speciality findById(Long id) {
        log.debug("Fetching speciality with id: {}", id);
        Speciality speciality = specialityRepository.findById(id).orElse(null);
        if (speciality == null) {
            log.warn("Speciality not found with id: {}", id);
        }
        return speciality;
    }

    @Transactional
    public Speciality save(Speciality entity) {
        log.debug("Saving speciality: {}", entity.getName());
        Speciality savedSpeciality = specialityRepository.save(entity);
        log.info("Speciality saved successfully with id: {}", savedSpeciality.getId());
        return savedSpeciality;
    }

    @Transactional
    public Speciality update(Long id, Speciality entity) {
        log.debug("Updating speciality with id: {}", id);
        if (!specialityRepository.existsById(id)) {
            log.warn("Speciality not found for update with id: {}", id);
            return null;
        }
        entity.setId(id);
        Speciality updatedSpeciality = specialityRepository.save(entity);
        log.info("Speciality updated successfully with id: {}", updatedSpeciality.getId());
        return updatedSpeciality;
    }

    @Transactional
    public void delete(Long id) {
        log.debug("Deleting speciality with id: {}", id);
        specialityRepository.deleteById(id);
        log.info("Speciality deleted successfully with id: {}", id);
    }
}
