package com.nttdata.finance_api.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.nttdata.finance_api.config.security.JwtAuthenticationFilter;
import com.nttdata.finance_api.dto.ExchangeRateResponse;
import com.nttdata.finance_api.service.ExchangeRateService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.time.LocalDate;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(ExchangeRateController.class)
@AutoConfigureMockMvc(addFilters = false)
class ExchangeRateControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    // 🔥 impede o Spring de instanciar o filtro real
    @MockBean
    private JwtAuthenticationFilter jwtAuthenticationFilter;

    @MockBean
    private ExchangeRateService exchangeRateService;

    @Test
    void shouldReturnExchangeRate() throws Exception {

        ExchangeRateResponse exchangeRate =
                new ExchangeRateResponse(
                        "USD",
                        LocalDate.of(2025, 1, 20),
                        BigDecimal.valueOf(5.12)
                );

        when(exchangeRateService.getExchangeRate("USD", "2025-01-20"))
                .thenReturn(exchangeRate);

        mockMvc.perform(get("/exchange-rate/USD/2025-01-20")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value(200))
                .andExpect(jsonPath("$.message")
                        .value("Cotação obtida com sucesso")) // ✅ AJUSTE AQUI
                .andExpect(jsonPath("$.data.currency").value("USD"))
                .andExpect(jsonPath("$.data.rate").value(5.12));
    }
}
