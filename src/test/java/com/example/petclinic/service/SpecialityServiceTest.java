package com.example.petclinic.service;

import com.example.petclinic.domain.entity.Speciality;
import com.example.petclinic.domain.repository.SpecialityRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validator;

import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
class SpecialityServiceTest {

    @Autowired
    private SpecialityService specialityService;

    @Autowired
    private SpecialityRepository specialityRepository;

    @Autowired
    private Validator validator;

    private Speciality speciality;

    @BeforeEach
    void setUp() {
        specialityRepository.deleteAll();
        speciality = new Speciality();
        speciality.setName("Cardiology");
    }

    @Test
    void shouldCreateSpeciality() {
        Speciality result = specialityService.save(speciality);

        assertNotNull(result);
        assertNotNull(result.getId());
        assertEquals("Cardiology", result.getName());
        assertEquals(1, specialityRepository.count());
    }

    @Test
    void shouldFindSpecialityById() {
        Speciality saved = specialityService.save(speciality);

        Speciality result = specialityService.findById(saved.getId());

        assertNotNull(result);
        assertEquals("Cardiology", result.getName());
    }

    @Test
    void shouldFindAllSpecialities() {
        specialityService.save(speciality);

        Speciality speciality2 = new Speciality();
        speciality2.setName("Dentistry");
        specialityService.save(speciality2);

        Speciality speciality3 = new Speciality();
        speciality3.setName("Surgery");
        specialityService.save(speciality3);

        List<Speciality> results = specialityService.findAll();

        assertNotNull(results);
        assertEquals(3, results.size());
        assertTrue(results.stream().anyMatch(s -> s.getName().equals("Cardiology")));
        assertTrue(results.stream().anyMatch(s -> s.getName().equals("Dentistry")));
        assertTrue(results.stream().anyMatch(s -> s.getName().equals("Surgery")));
    }

    @Test
    void shouldUpdateSpeciality() {
        Speciality saved = specialityService.save(speciality);

        saved.setName("Updated Cardiology");
        Speciality updated = specialityService.update(saved.getId(), saved);

        assertNotNull(updated);
        assertEquals("Updated Cardiology", updated.getName());
        assertEquals(1, specialityRepository.count());
    }

    @Test
    void shouldReturnNullWhenUpdatingNonExistentSpeciality() {
        speciality.setId(9999L);

        Speciality result = specialityService.update(9999L, speciality);

        assertNull(result);
    }

    @Test
    void shouldDeleteSpeciality() {
        Speciality saved = specialityService.save(speciality);
        Long id = saved.getId();

        specialityService.delete(id);

        assertNull(specialityService.findById(id));
        assertEquals(0, specialityRepository.count());
    }

    @Test
    void shouldValidateSpecialityNameNotBlank() {
        Speciality emptySpeciality = new Speciality();

        Set<ConstraintViolation<Speciality>> violations = validator.validate(emptySpeciality);

        assertTrue(violations.size() > 0);
        assertTrue(violations.stream().anyMatch(v -> v.getPropertyPath().toString().equals("name")));
    }

    @Test
    void shouldHandleMultipleSpecialities() {
        specialityService.save(speciality);

        Speciality speciality2 = new Speciality();
        speciality2.setName("Dentistry");
        specialityService.save(speciality2);

        Speciality speciality3 = new Speciality();
        speciality3.setName("Surgery");
        specialityService.save(speciality3);

        Speciality speciality4 = new Speciality();
        speciality4.setName("Oncology");
        specialityService.save(speciality4);

        List<Speciality> results = specialityService.findAll();

        assertEquals(4, results.size());
    }

    @Test
    void shouldFindSpecialityByIdAfterDelete() {
        Speciality saved1 = specialityService.save(speciality);

        Speciality speciality2 = new Speciality();
        speciality2.setName("Dentistry");
        Speciality saved2 = specialityService.save(speciality2);

        specialityService.delete(saved1.getId());

        assertNull(specialityService.findById(saved1.getId()));
        assertNotNull(specialityService.findById(saved2.getId()));
        assertEquals(1, specialityRepository.count());
    }

    @Test
    void shouldHandleDuplicateSpecialityNames() {
        specialityService.save(speciality);

        Speciality speciality2 = new Speciality();
        speciality2.setName("Cardiology");
        Speciality saved2 = specialityService.save(speciality2);

        List<Speciality> results = specialityService.findAll();

        assertEquals(2, results.size());
        assertTrue(results.stream().filter(s -> s.getName().equals("Cardiology")).count() >= 2);
    }
}
