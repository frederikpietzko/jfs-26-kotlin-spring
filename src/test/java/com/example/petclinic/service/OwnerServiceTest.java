package com.example.petclinic.service;

import com.example.petclinic.domain.entity.Owner;
import com.example.petclinic.domain.repository.OwnerRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class OwnerServiceTest {

    @Mock
    private OwnerRepository ownerRepository;

    @InjectMocks
    private OwnerService ownerService;

    private final Validator validator = Validation.buildDefaultValidatorFactory().getValidator();

    private Owner owner;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
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

        assertEquals(4, violations.size());
        assertTrue(violations.stream().anyMatch(v -> v.getPropertyPath().toString().equals("firstName")));
        assertTrue(violations.stream().anyMatch(v -> v.getPropertyPath().toString().equals("lastName")));
    }

    @Test
    void shouldCreateOwner() {
        when(ownerRepository.save(any(Owner.class))).thenAnswer(invocation -> invocation.getArgument(0));

        Owner result = ownerService.save(owner);

        assertNotNull(result);
        assertEquals("John", result.getFirstName());
        assertEquals("Doe", result.getLastName());
        verify(ownerRepository, times(1)).save(any(Owner.class));
    }

    @Test
    void shouldFindAllOwners() {
        when(ownerRepository.findAll()).thenReturn(List.of(owner));

        List<Owner> results = ownerService.findAll();

        assertNotNull(results);
        assertEquals(1, results.size());
        assertEquals("John", results.get(0).getFirstName());
    }
}
