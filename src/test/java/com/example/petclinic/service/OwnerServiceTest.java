package com.example.petclinic.service;

import com.example.petclinic.domain.entity.Owner;
import com.example.petclinic.domain.repository.OwnerRepository;
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
class OwnerServiceTest {

    @Autowired
    private OwnerService ownerService;

    @Autowired
    private OwnerRepository ownerRepository;

    @Autowired
    private Validator validator;

    private Owner owner;

    @BeforeEach
    void setUp() {
        ownerRepository.deleteAll();
        owner = new Owner();
        owner.setFirstName("John");
        owner.setLastName("Doe");
        owner.setAddress("123 Main St");
        owner.setCity("Springfield");
        owner.setTelephone("123456789");
    }

    @Test
    void shouldValidateOwnerRequest_emptyDTO_failsValidation() {
        Owner emptyOwner = new Owner();

        Set<ConstraintViolation<Owner>> violations = validator.validate(emptyOwner);

        assertTrue(violations.size() >= 2);
        assertTrue(violations.stream().anyMatch(v -> v.getPropertyPath().toString().equals("firstName")));
        assertTrue(violations.stream().anyMatch(v -> v.getPropertyPath().toString().equals("lastName")));
    }

    @Test
    void shouldFindOwnerById() {
        Owner saved = ownerService.save(owner);

        Owner result = ownerService.findById(saved.getId());

        assertNotNull(result);
        assertEquals("John", result.getFirstName());
        assertEquals("Doe", result.getLastName());
    }

    @Test
    void shouldUpdateOwner() {
        Owner saved = ownerService.save(owner);
        saved.setFirstName("Johnny");
        saved.setLastName("Smith");

        Owner updated = ownerService.update(saved.getId(), saved);

        assertNotNull(updated);
        assertEquals("Johnny", updated.getFirstName());
        assertEquals("Smith", updated.getLastName());
        assertEquals(1, ownerRepository.count());
    }

    @Test
    void shouldDeleteOwner() {
        Owner saved = ownerService.save(owner);
        Long id = saved.getId();

        ownerService.delete(id);

        assertNull(ownerService.findById(id));
        assertEquals(0, ownerRepository.count());
    }

    @Test
    void shouldCreateOwner() {
        Owner result = ownerService.save(owner);

        assertNotNull(result);
        assertNotNull(result.getId());
        assertEquals("John", result.getFirstName());
        assertEquals("Doe", result.getLastName());
        assertEquals(1, ownerRepository.count());
    }

    @Test
    void shouldFindAllOwners() {
        ownerService.save(owner);
        Owner owner2 = new Owner();
        owner2.setFirstName("Jane");
        owner2.setLastName("Smith");
        owner2.setAddress("456 Oak Ave");
        owner2.setCity("Shelbyville");
        owner2.setTelephone("987654321");
        ownerService.save(owner2);

        List<Owner> results = ownerService.findAll();

        assertNotNull(results);
        assertEquals(2, results.size());
        assertEquals("John", results.get(0).getFirstName());
        assertEquals("Jane", results.get(1).getFirstName());
    }
}
