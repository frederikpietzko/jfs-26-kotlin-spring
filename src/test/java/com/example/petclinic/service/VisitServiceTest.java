package com.example.petclinic.service;

import com.example.petclinic.domain.entity.Owner;
import com.example.petclinic.domain.entity.Pet;
import com.example.petclinic.domain.entity.PetType;
import com.example.petclinic.domain.entity.Visit;
import com.example.petclinic.domain.repository.OwnerRepository;
import com.example.petclinic.domain.repository.PetRepository;
import com.example.petclinic.domain.repository.VisitRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
class VisitServiceTest {

    @Autowired
    private VisitService visitService;

    @Autowired
    private VisitRepository visitRepository;

    @Autowired
    private PetRepository petRepository;

    @Autowired
    private OwnerRepository ownerRepository;

    private Owner owner;
    private Pet pet;
    private Visit visit;

    @BeforeEach
    void setUp() {
        visitRepository.deleteAll();
        petRepository.deleteAll();
        ownerRepository.deleteAll();

        owner = new Owner();
        owner.setFirstName("John");
        owner.setLastName("Doe");
        owner.setAddress("123 Main St");
        owner.setCity("Springfield");
        owner.setTelephone("123456789");
        owner = ownerRepository.save(owner);

        pet = new Pet();
        pet.setName("Fluffy");
        pet.setType(PetType.CAT);
        pet.setBirthDate(LocalDate.of(2020, 1, 15));
        pet.setOwner(owner);
        pet = petRepository.save(pet);

        visit = new Visit();
        visit.setDate(LocalDate.of(2026, 7, 7));
        visit.setDescription("Routine checkup");
        visit.setPet(pet);
    }

    @Test
    void shouldCreateVisit() {
        Visit result = visitService.save(visit);

        assertNotNull(result);
        assertNotNull(result.getId());
        assertEquals("Routine checkup", result.getDescription());
        assertEquals(LocalDate.of(2026, 7, 7), result.getDate());
        assertEquals(1, visitRepository.count());
    }

    @Test
    void shouldFindVisitById() {
        Visit saved = visitService.save(visit);

        Visit result = visitService.findById(saved.getId());

        assertNotNull(result);
        assertEquals("Routine checkup", result.getDescription());
        assertEquals(pet.getId(), result.getPet().getId());
    }

    @Test
    void shouldFindVisitsByPetId() {
        Visit visit1 = visitService.save(visit);

        Visit visit2 = new Visit();
        visit2.setDate(LocalDate.of(2026, 6, 15));
        visit2.setDescription("Vaccination");
        visit2.setPet(pet);
        Visit saved2 = visitService.save(visit2);

        List<Visit> results = visitService.findByPetId(pet.getId());

        assertNotNull(results);
        assertEquals(2, results.size());
        assertTrue(results.stream().anyMatch(v -> v.getDescription().equals("Routine checkup")));
        assertTrue(results.stream().anyMatch(v -> v.getDescription().equals("Vaccination")));
    }

    @Test
    void shouldFindByPetIdReturnEmptyForNonExistentPet() {
        List<Visit> results = visitService.findByPetId(9999L);

        assertNotNull(results);
        assertEquals(0, results.size());
    }

    @Test
    void shouldUpdateVisit() {
        Visit saved = visitService.save(visit);

        saved.setDescription("Updated description");
        saved.setDate(LocalDate.of(2026, 7, 8));
        Visit updated = visitService.update(saved.getId(), saved);

        assertNotNull(updated);
        assertEquals("Updated description", updated.getDescription());
        assertEquals(LocalDate.of(2026, 7, 8), updated.getDate());
        assertEquals(1, visitRepository.count());
    }

    @Test
    void shouldReturnNullWhenUpdatingNonExistentVisit() {
        visit.setId(9999L);

        Visit result = visitService.update(9999L, visit);

        assertNull(result);
    }

    @Test
    void shouldReturnNullWhenSavingVisitWithNonExistentPet() {
        visit.setPet(new Pet());
        visit.getPet().setId(9999L);

        Visit result = visitService.save(visit);

        assertNull(result);
    }

    @Test
    void shouldDeleteVisit() {
        Visit saved = visitService.save(visit);
        Long id = saved.getId();

        visitService.delete(id);

        assertNull(visitService.findById(id));
        assertEquals(0, visitRepository.count());
    }

    @Test
    void shouldHandleMultipleVisitsForSamePet() {
        Visit visit1 = visitService.save(visit);

        Visit visit2 = new Visit();
        visit2.setDate(LocalDate.of(2026, 6, 15));
        visit2.setDescription("Vaccination");
        visit2.setPet(pet);
        Visit saved2 = visitService.save(visit2);

        Visit visit3 = new Visit();
        visit3.setDate(LocalDate.of(2026, 5, 10));
        visit3.setDescription("Dental cleaning");
        visit3.setPet(pet);
        Visit saved3 = visitService.save(visit3);

        List<Visit> results = visitService.findByPetId(pet.getId());

        assertEquals(3, results.size());
    }

    @Test
    void shouldHandleVisitsForMultiplePets() {
        Visit petVisit1 = visitService.save(visit);

        Pet pet2 = new Pet();
        pet2.setName("Buddy");
        pet2.setType(PetType.DOG);
        pet2.setBirthDate(LocalDate.of(2019, 5, 20));
        pet2.setOwner(owner);
        pet2 = petRepository.save(pet2);

        Visit petVisit2 = new Visit();
        petVisit2.setDate(LocalDate.of(2026, 7, 7));
        petVisit2.setDescription("Check teeth");
        petVisit2.setPet(pet2);
        Visit saved2 = visitService.save(petVisit2);

        List<Visit> pet1Visits = visitService.findByPetId(pet.getId());
        List<Visit> pet2Visits = visitService.findByPetId(pet2.getId());

        assertEquals(1, pet1Visits.size());
        assertEquals(1, pet2Visits.size());
        assertEquals("Routine checkup", pet1Visits.get(0).getDescription());
        assertEquals("Check teeth", pet2Visits.get(0).getDescription());
    }

    @Test
    void shouldPreservePetReferenceOnUpdate() {
        Visit saved = visitService.save(visit);

        saved.setDescription("New description");
        Visit updated = visitService.update(saved.getId(), saved);

        assertEquals(pet.getId(), updated.getPet().getId());
        assertEquals("Fluffy", updated.getPet().getName());
    }
}
