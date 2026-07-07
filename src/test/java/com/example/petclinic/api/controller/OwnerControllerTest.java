package com.example.petclinic.api.controller;

import com.example.petclinic.api.dto.OwnerRequest;
import com.example.petclinic.domain.entity.Owner;
import com.example.petclinic.domain.repository.OwnerRepository;

import java.util.HashSet;
import tools.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.hasSize;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest(properties = {
    "spring.profiles.active=test"
})
@AutoConfigureMockMvc
@Import(TestSecurityConfig.class)
class OwnerControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private OwnerRepository ownerRepository;

    private ObjectMapper objectMapper;
    private OwnerRequest request;

    @BeforeEach
    void setUp() {
        ownerRepository.deleteAll();
        objectMapper = new ObjectMapper();
        request = new OwnerRequest("John", "Doe", "123 Main St", "Springfield", "123456789");
    }

    @Test
    void shouldCreateOwner() throws Exception {
        mockMvc.perform(post("/owners")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated());

        assert ownerRepository.count() == 1;
        Owner saved = ownerRepository.findAll().getFirst();
        assert saved.getFirstName().equals("John");
        assert saved.getLastName().equals("Doe");
    }

    @Test
    void shouldReturnAllOwners() throws Exception {
        Owner owner = new Owner(null, "John", "Doe", "123 Main St", "Springfield", "123456789", new HashSet<>());
        ownerRepository.save(owner);
        Owner owner2 = new Owner(null, "Jane", "Smith", "456 Oak Ave", "Shelbyville", "987654321", new HashSet<>());
        ownerRepository.save(owner2);

        mockMvc.perform(get("/owners"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)));
    }

    @Test
    void shouldValidateEmptyRequest() throws Exception {
        OwnerRequest emptyRequest = new OwnerRequest(null, null, null, null, null);

        mockMvc.perform(post("/owners")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(emptyRequest)))
                .andExpect(status().isBadRequest());

        assert ownerRepository.count() == 0;
    }
}
