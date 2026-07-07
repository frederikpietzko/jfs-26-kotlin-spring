package com.example.petclinic.api.controller;

import com.example.petclinic.api.dto.VisitRequest;
import com.example.petclinic.domain.entity.Owner;
import com.example.petclinic.domain.entity.Pet;
import com.example.petclinic.domain.entity.PetType;
import com.example.petclinic.domain.entity.Visit;
import com.example.petclinic.domain.repository.OwnerRepository;
import com.example.petclinic.domain.repository.PetRepository;
import com.example.petclinic.domain.repository.VisitRepository;
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
class VisitControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private VisitRepository visitRepository;

    @Autowired
    private PetRepository petRepository;

    @Autowired
    private OwnerRepository ownerRepository;

    private ObjectMapper objectMapper;
    private Owner owner;
    private Pet pet;
    private VisitRequest visitRequest;

    @BeforeEach
    void setUp() {
        visitRepository.deleteAll();
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

        pet = new Pet();
        pet.setName("Fluffy");
        pet.setType(PetType.CAT);
        pet.setBirthDate(LocalDate.of(2020, 1, 15));
        pet.setOwner(owner);
        pet = petRepository.save(pet);

        visitRequest = new VisitRequest(pet.getId(), LocalDate.now(), "Routine checkup");
    }

    @Test
    void shouldCreateVisit() throws Exception {
        mockMvc.perform(post("/visits")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(visitRequest)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.description").value("Routine checkup"));

        assert visitRepository.count() == 1;
    }

    @Test
    void shouldGetVisitById() throws Exception {
        Visit visit = new Visit();
        visit.setDate(LocalDate.now());
        visit.setDescription("Routine checkup");
        visit.setPet(pet);
        visit = visitRepository.save(visit);

        mockMvc.perform(get("/visits/{id}", visit.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.description").value("Routine checkup"));
    }

    @Test
    void shouldReturnNotFoundForNonExistentVisit() throws Exception {
        mockMvc.perform(get("/visits/{id}", 9999L))
                .andExpect(status().isNotFound());
    }

    @Test
    void shouldGetAllVisits() throws Exception {
        Visit visit1 = new Visit();
        visit1.setDate(LocalDate.now());
        visit1.setDescription("Routine checkup");
        visit1.setPet(pet);
        visitRepository.save(visit1);

        Visit visit2 = new Visit();
        visit2.setDate(LocalDate.now().minusDays(1));
        visit2.setDescription("Vaccination");
        visit2.setPet(pet);
        visitRepository.save(visit2);

        mockMvc.perform(get("/visits"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].description").value("Routine checkup"))
                .andExpect(jsonPath("$[1].description").value("Vaccination"));
    }

    @Test
    void shouldReturnEmptyListWhenNoVisits() throws Exception {
        mockMvc.perform(get("/visits"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(0)));
    }

    @Test
    void shouldUpdateVisit() throws Exception {
        Visit visit = new Visit();
        visit.setDate(LocalDate.now());
        visit.setDescription("Routine checkup");
        visit.setPet(pet);
        visit = visitRepository.save(visit);

        VisitRequest updateRequest = new VisitRequest(pet.getId(), LocalDate.now().plusDays(1), "Updated description");

        mockMvc.perform(put("/visits/{id}", visit.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(updateRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.description").value("Updated description"));
    }

    @Test
    void shouldReturnNotFoundWhenUpdatingNonExistentVisit() throws Exception {
        VisitRequest updateRequest = new VisitRequest(pet.getId(), LocalDate.now(), "Updated");

        mockMvc.perform(put("/visits/{id}", 9999L)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(updateRequest)))
                .andExpect(status().isNotFound());
    }

    @Test
    void shouldDeleteVisit() throws Exception {
        Visit visit = new Visit();
        visit.setDate(LocalDate.now());
        visit.setDescription("Routine checkup");
        visit.setPet(pet);
        visit = visitRepository.save(visit);

        mockMvc.perform(delete("/visits/{id}", visit.getId()))
                .andExpect(status().isNoContent());

        assert visitRepository.count() == 0;
    }

    @Test
    void shouldValidateEmptyVisitRequest() throws Exception {
        VisitRequest emptyRequest = new VisitRequest(pet.getId(), null, null);

        mockMvc.perform(post("/visits")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(emptyRequest)))
                .andExpect(status().isBadRequest());

        assert visitRepository.count() == 0;
    }

    @Test
    void shouldValidateVisitDescriptionNotBlank() throws Exception {
        VisitRequest invalidRequest = new VisitRequest(pet.getId(), LocalDate.now(), "");

        mockMvc.perform(post("/visits")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(invalidRequest)))
                .andExpect(status().isBadRequest());

        assert visitRepository.count() == 0;
    }

    @Test
    void shouldHandleMultipleVisitsForSamePet() throws Exception {
        VisitRequest visit1Request = new VisitRequest(pet.getId(), LocalDate.now(), "Routine checkup");
        VisitRequest visit2Request = new VisitRequest(pet.getId(), LocalDate.now().minusDays(1), "Vaccination");
        VisitRequest visit3Request = new VisitRequest(pet.getId(), LocalDate.now().minusDays(2), "Dental cleaning");

        mockMvc.perform(post("/visits")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(visit1Request)))
                .andExpect(status().isCreated());

        mockMvc.perform(post("/visits")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(visit2Request)))
                .andExpect(status().isCreated());

        mockMvc.perform(post("/visits")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(visit3Request)))
                .andExpect(status().isCreated());

        mockMvc.perform(get("/visits"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(3)));
    }

    @Test
    void shouldHandleVisitsForMultiplePets() throws Exception {
        Pet pet2 = new Pet();
        pet2.setName("Buddy");
        pet2.setType(PetType.DOG);
        pet2.setBirthDate(LocalDate.of(2019, 5, 20));
        pet2.setOwner(owner);
        pet2 = petRepository.save(pet2);

        VisitRequest pet1VisitRequest = new VisitRequest(pet.getId(), LocalDate.now(), "Cat checkup");
        VisitRequest pet2VisitRequest = new VisitRequest(pet2.getId(), LocalDate.now(), "Dog checkup");

        mockMvc.perform(post("/visits")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(pet1VisitRequest)))
                .andExpect(status().isCreated());

        mockMvc.perform(post("/visits")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(pet2VisitRequest)))
                .andExpect(status().isCreated());

        mockMvc.perform(get("/visits"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)));
    }
}
