package com.nttdata.finance_api.controller;

import com.nttdata.finance_api.config.security.JwtUtil;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(HealthController.class)
@AutoConfigureMockMvc(addFilters = false) // 🔥 desliga security no teste
class HealthControllerTest {

    @Autowired
    private MockMvc mockMvc;

    // 🔥 Necessário por causa do JwtAuthenticationFilter
    @MockBean
    private JwtUtil jwtUtil;

    @Test
    void shouldReturnApplicationHealth() throws Exception {

        mockMvc.perform(get("/health"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value(200))
                .andExpect(jsonPath("$.message").value("Aplicação no ar"))
                .andExpect(jsonPath("$.data").value("OK"));
    }
}
