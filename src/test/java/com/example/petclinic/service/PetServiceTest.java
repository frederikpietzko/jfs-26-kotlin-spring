package com.example.petclinic.service;

import com.example.petclinic.domain.entity.Owner;
import com.example.petclinic.domain.entity.Pet;
import com.example.petclinic.domain.entity.PetType;
import com.example.petclinic.domain.repository.OwnerRepository;
import com.example.petclinic.domain.repository.PetRepository;
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
class PetServiceTest {

    @Autowired
    private PetService petService;

    @Autowired
    private PetRepository petRepository;

    @Autowired
    private OwnerRepository ownerRepository;

    private Owner owner;
    private Pet pet;

    @BeforeEach
    void setUp() {
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
    }

    @Test
    void shouldCreatePet() {
        Pet result = petService.save(pet);

        assertNotNull(result);
        assertNotNull(result.getId());
        assertEquals("Fluffy", result.getName());
        assertEquals(PetType.CAT, result.getType());
        assertEquals(1, petRepository.count());
    }

    @Test
    void shouldFindPetById() {
        Pet saved = petService.save(pet);

        Pet result = petService.findById(saved.getId());

        assertNotNull(result);
        assertEquals("Fluffy", result.getName());
        assertEquals(PetType.CAT, result.getType());
    }

    @Test
    void shouldFindPetsByOwnerId() {
        Pet pet1 = petService.save(pet);

        Pet pet2 = new Pet();
        pet2.setName("Buddy");
        pet2.setType(PetType.DOG);
        pet2.setBirthDate(LocalDate.of(2019, 5, 20));
        pet2.setOwner(owner);
        pet2 = petService.save(pet2);

        List<Pet> results = petService.findByOwnerId(owner.getId());

        assertNotNull(results);
        assertEquals(2, results.size());
        assertTrue(results.stream().anyMatch(p -> p.getName().equals("Fluffy")));
        assertTrue(results.stream().anyMatch(p -> p.getName().equals("Buddy")));
    }

    @Test
    void shouldFindByOwnerIdReturnEmptyForNonExistentOwner() {
        List<Pet> results = petService.findByOwnerId(9999L);

        assertNotNull(results);
        assertEquals(0, results.size());
    }

    @Test
    void shouldUpdatePet() {
        Pet saved = petService.save(pet);

        saved.setName("FluffyUpdated");
        saved.setType(PetType.DOG);
        Pet updated = petService.update(saved.getId(), saved);

        assertNotNull(updated);
        assertEquals("FluffyUpdated", updated.getName());
        assertEquals(PetType.DOG, updated.getType());
        assertEquals(1, petRepository.count());
    }

    @Test
    void shouldReturnNullWhenUpdatingNonExistentPet() {
        pet.setId(9999L);

        Pet result = petService.update(9999L, pet);

        assertNull(result);
    }

    @Test
    void shouldReturnNullWhenSavingPetWithNonExistentOwner() {
        pet.setOwner(new Owner());
        pet.getOwner().setId(9999L);

        Pet result = petService.save(pet);

        assertNull(result);
    }

    @Test
    void shouldDeletePet() {
        Pet saved = petService.save(pet);
        Long id = saved.getId();

        petService.delete(id);

        assertNull(petService.findById(id));
        assertEquals(0, petRepository.count());
    }

    @Test
    void shouldHandleMultiplePetsForSameOwner() {
        Pet savedPet1 = petService.save(pet);

        Owner owner2 = new Owner();
        owner2.setFirstName("Jane");
        owner2.setLastName("Smith");
        owner2.setAddress("456 Oak Ave");
        owner2.setCity("Shelbyville");
        owner2.setTelephone("987654321");
        owner2 = ownerRepository.save(owner2);

        Pet pet2 = new Pet();
        pet2.setName("Max");
        pet2.setType(PetType.DOG);
        pet2.setBirthDate(LocalDate.of(2021, 3, 10));
        pet2.setOwner(owner2);
        Pet savedPet2 = petService.save(pet2);

        List<Pet> owner1Pets = petService.findByOwnerId(owner.getId());
        List<Pet> owner2Pets = petService.findByOwnerId(owner2.getId());

        assertEquals(1, owner1Pets.size());
        assertEquals(1, owner2Pets.size());
        assertEquals("Fluffy", owner1Pets.get(0).getName());
        assertEquals("Max", owner2Pets.get(0).getName());
    }
}
