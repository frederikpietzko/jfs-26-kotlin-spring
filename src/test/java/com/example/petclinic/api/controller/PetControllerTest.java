package com.example.petclinic.api.controller;

import com.example.petclinic.api.dto.PetRequest;
import com.example.petclinic.api.dto.PetTypeRequest;
import com.example.petclinic.domain.entity.Owner;
import com.example.petclinic.domain.entity.Pet;
import com.example.petclinic.domain.entity.PetType;
import com.example.petclinic.domain.repository.OwnerRepository;
import com.example.petclinic.domain.repository.PetRepository;
import tools.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;
import java.util.HashSet;

import static org.hamcrest.Matchers.hasSize;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest(properties = {"spring.profiles.active=test"})
@AutoConfigureMockMvc
@Import(TestSecurityConfig.class)
class PetControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private PetRepository petRepository;

    @Autowired
    private OwnerRepository ownerRepository;

    private ObjectMapper objectMapper;
    private Owner owner;
    private PetRequest petRequest;

    @BeforeEach
    void setUp() {
        petRepository.deleteAll();
        ownerRepository.deleteAll();
        objectMapper = new ObjectMapper();

        owner = new Owner();
        owner.setFirstName("John");
        owner.setLastName("Doe");
        owner.setAddress("123 Main St");
        owner.setCity("Springfield");
        owner.setTelephone("123456789");
        owner = ownerRepository.save(owner);

        petRequest = new PetRequest("Fluffy", PetTypeRequest.CAT);
    }

    @Test
    void shouldCreatePet() throws Exception {
        mockMvc.perform(post("/owners/{ownerId}/pets", owner.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(petRequest)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.name").value("Fluffy"))
                .andExpect(jsonPath("$.type").value("CAT"));

        assert petRepository.count() == 1;
    }

    @Test
    void shouldGetPetById() throws Exception {
        Pet pet = new Pet();
        pet.setName("Fluffy");
        pet.setType(PetType.CAT);
        pet.setOwner(owner);
        pet = petRepository.save(pet);

        mockMvc.perform(get("/owners/{ownerId}/pets/{id}", owner.getId(), pet.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Fluffy"))
                .andExpect(jsonPath("$.type").value("CAT"));
    }

    @Test
    void shouldReturnNotFoundForNonExistentPet() throws Exception {
        mockMvc.perform(get("/owners/{ownerId}/pets/{id}", owner.getId(), 9999L))
                .andExpect(status().isNotFound());
    }

    @Test
    void shouldGetPetsByOwnerId() throws Exception {
        Pet pet1 = new Pet();
        pet1.setName("Fluffy");
        pet1.setType(PetType.CAT);
        pet1.setOwner(owner);
        petRepository.save(pet1);

        Pet pet2 = new Pet();
        pet2.setName("Buddy");
        pet2.setType(PetType.DOG);
        pet2.setOwner(owner);
        petRepository.save(pet2);

        mockMvc.perform(get("/owners/{ownerId}/pets", owner.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].name").value("Fluffy"))
                .andExpect(jsonPath("$[1].name").value("Buddy"));
    }

    @Test
    void shouldReturnEmptyListForOwnerWithNoPets() throws Exception {
        mockMvc.perform(get("/owners/{ownerId}/pets", owner.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(0)));
    }

    @Test
    void shouldUpdatePet() throws Exception {
        Pet pet = new Pet();
        pet.setName("Fluffy");
        pet.setType(PetType.CAT);
        pet.setOwner(owner);
        pet = petRepository.save(pet);

        PetRequest updateRequest = new PetRequest("FluffyUpdated", PetTypeRequest.DOG);

        mockMvc.perform(put("/owners/{ownerId}/pets/{id}", owner.getId(), pet.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(updateRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("FluffyUpdated"))
                .andExpect(jsonPath("$.type").value("DOG"));
    }

    @Test
    void shouldReturnNotFoundWhenUpdatingNonExistentPet() throws Exception {
        PetRequest updateRequest = new PetRequest("Updated", PetTypeRequest.CAT);

        mockMvc.perform(put("/owners/{ownerId}/pets/{id}", owner.getId(), 9999L)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(updateRequest)))
                .andExpect(status().isNotFound());
    }

    @Test
    void shouldDeletePet() throws Exception {
        Pet pet = new Pet();
        pet.setName("Fluffy");
        pet.setType(PetType.CAT);
        pet.setOwner(owner);
        pet = petRepository.save(pet);

        mockMvc.perform(delete("/owners/{ownerId}/pets/{id}", owner.getId(), pet.getId()))
                .andExpect(status().isNoContent());

        assert petRepository.count() == 0;
    }

    @Test
    void shouldValidateEmptyPetRequest() throws Exception {
        PetRequest emptyRequest = new PetRequest(null, null);

        mockMvc.perform(post("/owners/{ownerId}/pets", owner.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(emptyRequest)))
                .andExpect(status().isBadRequest());

        assert petRepository.count() == 0;
    }

    @Test
    void shouldValidatePetNameNotBlank() throws Exception {
        PetRequest invalidRequest = new PetRequest("", PetTypeRequest.CAT);

        mockMvc.perform(post("/owners/{ownerId}/pets", owner.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(invalidRequest)))
                .andExpect(status().isBadRequest());

        assert petRepository.count() == 0;
    }

    @Test
    void shouldHandleMultiplePetsForSameOwner() throws Exception {
        PetRequest petRequest1 = new PetRequest("Fluffy", PetTypeRequest.CAT);
        PetRequest petRequest2 = new PetRequest("Buddy", PetTypeRequest.DOG);
        PetRequest petRequest3 = new PetRequest("Nemo", PetTypeRequest.FISH);

        mockMvc.perform(post("/owners/{ownerId}/pets", owner.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(petRequest1)))
                .andExpect(status().isCreated());

        mockMvc.perform(post("/owners/{ownerId}/pets", owner.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(petRequest2)))
                .andExpect(status().isCreated());

        mockMvc.perform(post("/owners/{ownerId}/pets", owner.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(petRequest3)))
                .andExpect(status().isCreated());

        mockMvc.perform(get("/owners/{ownerId}/pets", owner.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(3)));
    }

    @Test
    void shouldHandlePetsForMultipleOwners() throws Exception {
        Owner owner2 = new Owner();
        owner2.setFirstName("Jane");
        owner2.setLastName("Smith");
        owner2.setAddress("456 Oak Ave");
        owner2.setCity("Shelbyville");
        owner2.setTelephone("987654321");
        owner2 = ownerRepository.save(owner2);

        mockMvc.perform(post("/owners/{ownerId}/pets", owner.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(petRequest)))
                .andExpect(status().isCreated());

        PetRequest petRequest2 = new PetRequest("Max", PetTypeRequest.DOG);
        mockMvc.perform(post("/owners/{ownerId}/pets", owner2.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(petRequest2)))
                .andExpect(status().isCreated());

        mockMvc.perform(get("/owners/{ownerId}/pets", owner.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].name").value("Fluffy"));

        mockMvc.perform(get("/owners/{ownerId}/pets", owner2.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].name").value("Max"));
    }
}
