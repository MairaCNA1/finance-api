package com.nttdata.finance_api.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.nttdata.finance_api.domain.User;
import com.nttdata.finance_api.service.UserService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(UserController.class)
class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserService userService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void shouldCreateUser() throws Exception {
        User user = new User("Nebulosa", "nebulosa@email.com");

        when(userService.create(org.mockito.ArgumentMatchers.any(User.class)))
                .thenReturn(user);

        mockMvc.perform(post("/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(user)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.data.name").value("Nebulosa"))
                .andExpect(jsonPath("$.data.email").value("nebulosa@email.com"));
    }

    @Test
    void shouldListUsers() throws Exception {
        User user = new User("Nebulosa", "nebulosa@email.com");

        when(userService.findAll()).thenReturn(List.of(user));

        mockMvc.perform(get("/users"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data[0].name").value("Nebulosa"));
    }

    @Test
    void shouldFindUserById() throws Exception {
        User user = new User("Nebulosa", "nebulosa@email.com");

        when(userService.findById(1L)).thenReturn(user);

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
