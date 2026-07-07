package com.example.petclinic.api.controller;

import com.example.petclinic.api.dto.VetRequest;
import com.example.petclinic.domain.entity.Speciality;
import com.example.petclinic.domain.entity.Vet;
import com.example.petclinic.domain.repository.SpecialityRepository;
import com.example.petclinic.domain.repository.VetRepository;
import tools.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.HashSet;
import java.util.List;

import static org.hamcrest.Matchers.hasSize;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest(properties = {"spring.profiles.active=test"})
@AutoConfigureMockMvc
@Import(TestSecurityConfig.class)
class VetControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private VetRepository vetRepository;

    @Autowired
    private SpecialityRepository specialityRepository;

    private ObjectMapper objectMapper;
    private Speciality cardiology;
    private Speciality dentistry;

    @BeforeEach
    void setUp() {
        vetRepository.deleteAll();
        specialityRepository.deleteAll();
        objectMapper = new ObjectMapper();

        cardiology = new Speciality();
        cardiology.setName("Cardiology");
        cardiology = specialityRepository.save(cardiology);

        dentistry = new Speciality();
        dentistry.setName("Dentistry");
        dentistry = specialityRepository.save(dentistry);
    }

    @Test
    void shouldCreateVet() throws Exception {
        VetRequest vetRequest = new VetRequest("John", "Smith", List.of(cardiology.getId()));

        mockMvc.perform(post("/vets")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(vetRequest)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.firstName").value("John"))
                .andExpect(jsonPath("$.lastName").value("Smith"))
                .andExpect(jsonPath("$.specialties", hasSize(1)));

        assert vetRepository.count() == 1;
    }

    @Test
    void shouldGetVetById() throws Exception {
        Vet vet = new Vet();
        vet.setFirstName("John");
        vet.setLastName("Smith");
        vet.setSpecialties(new HashSet<>(List.of(cardiology)));
        vet = vetRepository.save(vet);

        mockMvc.perform(get("/vets/{id}", vet.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.firstName").value("John"))
                .andExpect(jsonPath("$.lastName").value("Smith"))
                .andExpect(jsonPath("$.specialties", hasSize(1)));
    }

    @Test
    void shouldReturnNotFoundForNonExistentVet() throws Exception {
        mockMvc.perform(get("/vets/{id}", 9999L))
                .andExpect(status().isNotFound());
    }

    @Test
    void shouldGetAllVets() throws Exception {
        Vet vet1 = new Vet();
        vet1.setFirstName("John");
        vet1.setLastName("Smith");
        vet1.setSpecialties(new HashSet<>(List.of(cardiology)));
        vetRepository.save(vet1);

        Vet vet2 = new Vet();
        vet2.setFirstName("Jane");
        vet2.setLastName("Doe");
        vet2.setSpecialties(new HashSet<>(List.of(dentistry)));
        vetRepository.save(vet2);

        mockMvc.perform(get("/vets"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].firstName").value("John"))
                .andExpect(jsonPath("$[1].firstName").value("Jane"));
    }

    @Test
    void shouldReturnEmptyListWhenNoVets() throws Exception {
        mockMvc.perform(get("/vets"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(0)));
    }

    @Test
    void shouldUpdateVet() throws Exception {
        Vet vet = new Vet();
        vet.setFirstName("John");
        vet.setLastName("Smith");
        vet.setSpecialties(new HashSet<>(List.of(cardiology)));
        vet = vetRepository.save(vet);

        VetRequest updateRequest = new VetRequest("Johnny", "Doe", List.of(dentistry.getId()));

        mockMvc.perform(put("/vets/{id}", vet.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(updateRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.firstName").value("Johnny"))
                .andExpect(jsonPath("$.lastName").value("Doe"))
                .andExpect(jsonPath("$.specialties", hasSize(1)));
    }

    @Test
    void shouldReturnNotFoundWhenUpdatingNonExistentVet() throws Exception {
        VetRequest updateRequest = new VetRequest("Johnny", "Doe", List.of(cardiology.getId()));

        mockMvc.perform(put("/vets/{id}", 9999L)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(updateRequest)))
                .andExpect(status().isNotFound());
    }

    @Test
    void shouldDeleteVet() throws Exception {
        Vet vet = new Vet();
        vet.setFirstName("John");
        vet.setLastName("Smith");
        vet.setSpecialties(new HashSet<>(List.of(cardiology)));
        vet = vetRepository.save(vet);

        mockMvc.perform(delete("/vets/{id}", vet.getId()))
                .andExpect(status().isNoContent());

        assert vetRepository.count() == 0;
    }

    @Test
    void shouldValidateEmptyVetRequest() throws Exception {
        VetRequest emptyRequest = new VetRequest(null, null, null);

        mockMvc.perform(post("/vets")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(emptyRequest)))
                .andExpect(status().isBadRequest());

        assert vetRepository.count() == 0;
    }

    @Test
    void shouldValidateVetFirstNameNotBlank() throws Exception {
        VetRequest invalidRequest = new VetRequest("", "Smith", List.of(cardiology.getId()));

        mockMvc.perform(post("/vets")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(invalidRequest)))
                .andExpect(status().isBadRequest());

        assert vetRepository.count() == 0;
    }

    @Test
    void shouldCreateVetWithMultipleSpecialties() throws Exception {
        VetRequest vetRequest = new VetRequest("John", "Smith", List.of(cardiology.getId(), dentistry.getId()));

        mockMvc.perform(post("/vets")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(vetRequest)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.specialties", hasSize(2)));
    }

    @Test
    void shouldCreateVetWithoutSpecialties() throws Exception {
        VetRequest vetRequest = new VetRequest("John", "Smith", null);

        mockMvc.perform(post("/vets")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(vetRequest)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.firstName").value("John"))
                .andExpect(jsonPath("$.specialties", hasSize(0)));
    }

    @Test
    void shouldHandleMultipleVets() throws Exception {
        VetRequest vet1Request = new VetRequest("John", "Smith", List.of(cardiology.getId()));
        VetRequest vet2Request = new VetRequest("Jane", "Doe", List.of(dentistry.getId()));
        VetRequest vet3Request = new VetRequest("Bob", "Johnson", List.of(cardiology.getId(), dentistry.getId()));

        mockMvc.perform(post("/vets")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(vet1Request)))
                .andExpect(status().isCreated());

        mockMvc.perform(post("/vets")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(vet2Request)))
                .andExpect(status().isCreated());

        mockMvc.perform(post("/vets")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(vet3Request)))
                .andExpect(status().isCreated());

        mockMvc.perform(get("/vets"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(3)));
    }

    @Test
    void shouldUpdateVetSpecialties() throws Exception {
        Vet vet = new Vet();
        vet.setFirstName("John");
        vet.setLastName("Smith");
        vet.setSpecialties(new HashSet<>(List.of(cardiology)));
        vet = vetRepository.save(vet);

        VetRequest updateRequest = new VetRequest("John", "Smith", List.of(dentistry.getId()));

        mockMvc.perform(put("/vets/{id}", vet.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(updateRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.specialties", hasSize(1)));
    }
}
