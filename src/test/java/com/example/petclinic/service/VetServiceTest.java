package com.example.petclinic.service;

import com.example.petclinic.domain.entity.Speciality;
import com.example.petclinic.domain.entity.Vet;
import com.example.petclinic.domain.repository.SpecialityRepository;
import com.example.petclinic.domain.repository.VetRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
class VetServiceTest {

    @Autowired
    private VetService vetService;

    @Autowired
    private VetRepository vetRepository;

    @Autowired
    private SpecialityRepository specialityRepository;

    private Vet vet;
    private Speciality cardiology;
    private Speciality dentistry;

    @BeforeEach
    void setUp() {
        vetRepository.deleteAll();
        specialityRepository.deleteAll();

        cardiology = new Speciality();
        cardiology.setName("Cardiology");
        cardiology = specialityRepository.save(cardiology);

        dentistry = new Speciality();
        dentistry.setName("Dentistry");
        dentistry = specialityRepository.save(dentistry);

        vet = new Vet();
        vet.setFirstName("John");
        vet.setLastName("Smith");
        vet.setSpecialties(new HashSet<>());
    }

    @Test
    void shouldCreateVet() {
        vet.getSpecialties().add(cardiology);

        Vet result = vetService.save(vet);

        assertNotNull(result);
        assertNotNull(result.getId());
        assertEquals("John", result.getFirstName());
        assertEquals("Smith", result.getLastName());
        assertEquals(1, result.getSpecialties().size());
        assertEquals(1, vetRepository.count());
    }

    @Test
    void shouldFindVetById() {
        vet.getSpecialties().add(cardiology);
        Vet saved = vetService.save(vet);

        Vet result = vetService.findById(saved.getId());

        assertNotNull(result);
        assertEquals("John", result.getFirstName());
        assertEquals(1, result.getSpecialties().size());
    }

    @Test
    void shouldFindAllVets() {
        vet.getSpecialties().add(cardiology);
        vetService.save(vet);

        Vet vet2 = new Vet();
        vet2.setFirstName("Jane");
        vet2.setLastName("Doe");
        vet2.setSpecialties(Set.of(dentistry));
        vetService.save(vet2);

        List<Vet> results = vetService.findAll();

        assertNotNull(results);
        assertEquals(2, results.size());
        assertTrue(results.stream().anyMatch(v -> v.getFirstName().equals("John")));
        assertTrue(results.stream().anyMatch(v -> v.getFirstName().equals("Jane")));
    }

    @Test
    void shouldUpdateVet() {
        vet.getSpecialties().add(cardiology);
        Vet saved = vetService.save(vet);

        saved.setFirstName("Johnny");
        saved.getSpecialties().add(dentistry);
        Vet updated = vetService.update(saved.getId(), saved);

        assertNotNull(updated);
        assertEquals("Johnny", updated.getFirstName());
        assertEquals(2, updated.getSpecialties().size());
        assertTrue(updated.getSpecialties().stream().anyMatch(s -> s.getName().equals("Cardiology")));
        assertTrue(updated.getSpecialties().stream().anyMatch(s -> s.getName().equals("Dentistry")));
    }

    @Test
    void shouldReturnNullWhenUpdatingNonExistentVet() {
        Vet result = vetService.update(9999L, vet);

        assertNull(result);
    }

    @Test
    void shouldDeleteVet() {
        vet.getSpecialties().add(cardiology);
        Vet saved = vetService.save(vet);
        Long id = saved.getId();

        vetService.delete(id);

        assertNull(vetService.findById(id));
        assertEquals(0, vetRepository.count());
    }

    @Test
    void shouldCreateVetWithoutSpecialties() {
        Vet result = vetService.save(vet);

        assertNotNull(result);
        assertNotNull(result.getId());
        assertEquals("John", result.getFirstName());
        assertEquals(0, result.getSpecialties().size());
    }

    @Test
    void shouldManageSpecialtiesForVet() {
        vet.getSpecialties().add(cardiology);
        vet.getSpecialties().add(dentistry);

        Vet saved = vetService.save(vet);

        Vet retrieved = vetService.findById(saved.getId());
        assertEquals(2, retrieved.getSpecialties().size());

        retrieved.getSpecialties().clear();
        Vet updated = vetService.update(retrieved.getId(), retrieved);

        assertEquals(0, updated.getSpecialties().size());
    }

    @Test
    void shouldHandleMultipleVetsWithSameSpecialty() {
        vet.getSpecialties().add(cardiology);
        Vet vet1 = vetService.save(vet);

        Vet vet2 = new Vet();
        vet2.setFirstName("Jane");
        vet2.setLastName("Doe");
        vet2.setSpecialties(Set.of(cardiology));
        Vet saved2 = vetService.save(vet2);

        Vet retrieved1 = vetService.findById(vet1.getId());
        Vet retrieved2 = vetService.findById(saved2.getId());

        assertEquals(1, retrieved1.getSpecialties().size());
        assertEquals(1, retrieved2.getSpecialties().size());
        assertEquals("Cardiology", retrieved1.getSpecialties().iterator().next().getName());
        assertEquals("Cardiology", retrieved2.getSpecialties().iterator().next().getName());
    }
}
