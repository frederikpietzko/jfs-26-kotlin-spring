package com.example.petclinic.api.controller;

import com.example.petclinic.api.dto.SpecialityRequest;
import com.example.petclinic.domain.entity.Speciality;
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

import static org.hamcrest.Matchers.hasSize;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest(properties = {"spring.profiles.active=test"})
@AutoConfigureMockMvc
@Import(TestSecurityConfig.class)
class SpecialityControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private SpecialityRepository specialityRepository;

    @Autowired
    private VetRepository vetRepository;

    private ObjectMapper objectMapper;
    private SpecialityRequest specialityRequest;

    @BeforeEach
    void setUp() {
        vetRepository.deleteAll();
        specialityRepository.deleteAll();
        objectMapper = new ObjectMapper();
        specialityRequest = new SpecialityRequest("Cardiology");
    }

    @Test
    void shouldCreateSpeciality() throws Exception {
        mockMvc.perform(post("/specialities")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(specialityRequest)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.name").value("Cardiology"));

        assert specialityRepository.count() == 1;
    }

    @Test
    void shouldGetSpecialityById() throws Exception {
        Speciality speciality = new Speciality();
        speciality.setName("Cardiology");
        speciality = specialityRepository.save(speciality);

        mockMvc.perform(get("/specialities/{id}", speciality.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Cardiology"));
    }

    @Test
    void shouldReturnNotFoundForNonExistentSpeciality() throws Exception {
        mockMvc.perform(get("/specialities/{id}", 9999L))
                .andExpect(status().isNotFound());
    }

    @Test
    void shouldGetAllSpecialities() throws Exception {
        Speciality speciality1 = new Speciality();
        speciality1.setName("Cardiology");
        specialityRepository.save(speciality1);

        Speciality speciality2 = new Speciality();
        speciality2.setName("Dentistry");
        specialityRepository.save(speciality2);

        Speciality speciality3 = new Speciality();
        speciality3.setName("Surgery");
        specialityRepository.save(speciality3);

        mockMvc.perform(get("/specialities"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(3)))
                .andExpect(jsonPath("$[0].name").value("Cardiology"))
                .andExpect(jsonPath("$[1].name").value("Dentistry"))
                .andExpect(jsonPath("$[2].name").value("Surgery"));
    }

    @Test
    void shouldReturnEmptyListWhenNoSpecialities() throws Exception {
        mockMvc.perform(get("/specialities"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(0)));
    }

    @Test
    void shouldUpdateSpeciality() throws Exception {
        Speciality speciality = new Speciality();
        speciality.setName("Cardiology");
        speciality = specialityRepository.save(speciality);

        SpecialityRequest updateRequest = new SpecialityRequest("Advanced Cardiology");

        mockMvc.perform(put("/specialities/{id}", speciality.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(updateRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Advanced Cardiology"));
    }

    @Test
    void shouldReturnNotFoundWhenUpdatingNonExistentSpeciality() throws Exception {
        SpecialityRequest updateRequest = new SpecialityRequest("Updated");

        mockMvc.perform(put("/specialities/{id}", 9999L)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(updateRequest)))
                .andExpect(status().isNotFound());
    }

    @Test
    void shouldDeleteSpeciality() throws Exception {
        Speciality speciality = new Speciality();
        speciality.setName("Cardiology");
        speciality = specialityRepository.save(speciality);

        mockMvc.perform(delete("/specialities/{id}", speciality.getId()))
                .andExpect(status().isNoContent());

        assert specialityRepository.count() == 0;
    }

    @Test
    void shouldValidateEmptySpecialityRequest() throws Exception {
        SpecialityRequest emptyRequest = new SpecialityRequest(null);

        mockMvc.perform(post("/specialities")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(emptyRequest)))
                .andExpect(status().isBadRequest());

        assert specialityRepository.count() == 0;
    }

    @Test
    void shouldValidateSpecialityNameNotBlank() throws Exception {
        SpecialityRequest invalidRequest = new SpecialityRequest("");

        mockMvc.perform(post("/specialities")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(invalidRequest)))
                .andExpect(status().isBadRequest());

        assert specialityRepository.count() == 0;
    }

    @Test
    void shouldHandleMultipleSpecialities() throws Exception {
        SpecialityRequest req1 = new SpecialityRequest("Cardiology");
        SpecialityRequest req2 = new SpecialityRequest("Dentistry");
        SpecialityRequest req3 = new SpecialityRequest("Surgery");
        SpecialityRequest req4 = new SpecialityRequest("Oncology");

        mockMvc.perform(post("/specialities")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(req1)))
                .andExpect(status().isCreated());

        mockMvc.perform(post("/specialities")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(req2)))
                .andExpect(status().isCreated());

        mockMvc.perform(post("/specialities")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(req3)))
                .andExpect(status().isCreated());

        mockMvc.perform(post("/specialities")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(req4)))
                .andExpect(status().isCreated());

        mockMvc.perform(get("/specialities"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(4)));
    }

    @Test
    void shouldAllowDuplicateSpecialityNames() throws Exception {
        SpecialityRequest req1 = new SpecialityRequest("Cardiology");
        SpecialityRequest req2 = new SpecialityRequest("Cardiology");

        mockMvc.perform(post("/specialities")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(req1)))
                .andExpect(status().isCreated());

        mockMvc.perform(post("/specialities")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(req2)))
                .andExpect(status().isCreated());

        mockMvc.perform(get("/specialities"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)));
    }

    @Test
    void shouldFetchSpecialityByIdAfterDelete() throws Exception {
        Speciality speciality1 = new Speciality();
        speciality1.setName("Cardiology");
        speciality1 = specialityRepository.save(speciality1);

        Speciality speciality2 = new Speciality();
        speciality2.setName("Dentistry");
        speciality2 = specialityRepository.save(speciality2);

        mockMvc.perform(delete("/specialities/{id}", speciality1.getId()))
                .andExpect(status().isNoContent());

        mockMvc.perform(get("/specialities/{id}", speciality1.getId()))
                .andExpect(status().isNotFound());

        mockMvc.perform(get("/specialities/{id}", speciality2.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Dentistry"));
    }

    @Test
    void shouldUpdateSpecialityPreservesId() throws Exception {
        Speciality speciality = new Speciality();
        speciality.setName("Cardiology");
        speciality = specialityRepository.save(speciality);
        Long originalId = speciality.getId();

        SpecialityRequest updateRequest = new SpecialityRequest("Updated Cardiology");

        mockMvc.perform(put("/specialities/{id}", originalId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(updateRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(originalId.intValue()))
                .andExpect(jsonPath("$.name").value("Updated Cardiology"));
    }
}
