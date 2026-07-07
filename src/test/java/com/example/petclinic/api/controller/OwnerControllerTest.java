package com.example.petclinic.api.controller;

import com.example.petclinic.api.dto.OwnerRequest;
import com.example.petclinic.domain.entity.Owner;
import com.example.petclinic.service.OwnerService;
import tools.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Collections;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(controllers = OwnerController.class)
@Import(TestSecurityConfig.class)
class OwnerControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private OwnerService ownerService;

    private ObjectMapper objectMapper;
    private OwnerRequest request;

    @BeforeEach
    void setUp() {
        objectMapper = new ObjectMapper();
        request = new OwnerRequest("John", "Doe", "123 Main St", "Springfield", "123456789");
    }

    @Test
    void shouldCreateOwner() throws Exception {
        when(ownerService.save(any(Owner.class))).thenReturn(null);

        mockMvc.perform(post("/owners")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated());

        verify(ownerService, times(1)).save(any(Owner.class));
    }

    @Test
    void shouldReturnAllOwners() throws Exception {
        when(ownerService.findAll()).thenReturn(Collections.emptyList());

        mockMvc.perform(get("/owners"))
                .andExpect(status().isOk());

        verify(ownerService, times(1)).findAll();
    }

    @Test
    void shouldValidateEmptyRequest() throws Exception {
        OwnerRequest emptyRequest = new OwnerRequest(null, null, null, null, null);

        mockMvc.perform(post("/owners")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(emptyRequest)))
                .andExpect(status().isBadRequest());
    }
}
