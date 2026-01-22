package com.nttdata.finance_api.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.nttdata.finance_api.dto.ExchangeRateResponse;
import com.nttdata.finance_api.service.ExchangeRateService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
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
class ExchangeRateControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ExchangeRateService exchangeRateService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void shouldReturnExchangeRate() throws Exception {

        ExchangeRateResponse response =
                new ExchangeRateResponse(
                        "USD",
                        LocalDate.of(2025, 2, 13),
                        BigDecimal.valueOf(5.12)
                );

        when(exchangeRateService.getExchangeRate("USD", "2025-02-13"))
                .thenReturn(response);

        mockMvc.perform(get("/exchange-rate/USD/2025-02-13")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.currency").value("USD"))
                .andExpect(jsonPath("$.data.rate").value(5.12))
                .andExpect(jsonPath("$.data.date").value("2025-02-13"));
    }
}
