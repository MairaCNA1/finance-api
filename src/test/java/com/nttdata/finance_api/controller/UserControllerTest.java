package com.nttdata.finance_api.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.nttdata.finance_api.config.security.JwtAuthenticationFilter;
import com.nttdata.finance_api.config.security.JwtUtil;
import com.nttdata.finance_api.domain.Role;
import com.nttdata.finance_api.domain.User;
import com.nttdata.finance_api.dto.CreateUserRequest;
import com.nttdata.finance_api.service.UserService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(UserController.class)
@AutoConfigureMockMvc(addFilters = false)
class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserService userService;

    @MockBean
    private JwtUtil jwtUtil;

    @MockBean
    private JwtAuthenticationFilter jwtAuthenticationFilter;

    @Autowired
    private ObjectMapper objectMapper;

    private User mockUser() {
        return new User(
                "Nebulosa",
                "nebulosa@email.com",
                "senhaFake",
                Role.USER
        );
    }

    @Test
    void shouldCreateUser() throws Exception {

        String json = """
            {
              "name": "Nebulosa",
              "email": "nebulosa@email.com",
              "password": "senhaFake"
            }
        """;

        when(userService.create(any(CreateUserRequest.class)))
                .thenReturn(mockUser());

        mockMvc.perform(post("/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.data.name").value("Nebulosa"))
                .andExpect(jsonPath("$.data.email").value("nebulosa@email.com"));
    }

    @Test
    void shouldListUsers() throws Exception {

        when(userService.findAll())
                .thenReturn(List.of(mockUser()));

        mockMvc.perform(get("/users"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data[0].name").value("Nebulosa"));
    }

    @Test
    void shouldFindUserById() throws Exception {

        when(userService.findById(1L))
                .thenReturn(mockUser());

        mockMvc.perform(get("/users/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.email").value("nebulosa@email.com"));
    }

    @Test
    void shouldDeleteUser() throws Exception {

        mockMvc.perform(delete("/users/1"))
                .andExpect(status().isNoContent());
    }
}