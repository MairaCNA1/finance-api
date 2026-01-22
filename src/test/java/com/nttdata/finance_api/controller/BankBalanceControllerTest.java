package com.nttdata.finance_api.controller;

import com.nttdata.finance_api.dto.BankBalanceResponse;
import com.nttdata.finance_api.service.BankBalanceService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(BankBalanceController.class)
class BankBalanceControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private BankBalanceService bankBalanceService;

    @Test
    void shouldReturnBankBalanceByUser() throws Exception {

        BankBalanceResponse response =
                new BankBalanceResponse(1L, BigDecimal.valueOf(500), "BRL");

        when(bankBalanceService.getBalanceByUser(1L))
                .thenReturn(response);

        mockMvc.perform(get("/balance/1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.userId").value(1))
                .andExpect(jsonPath("$.data.balance").value(500))
                .andExpect(jsonPath("$.data.currency").value("BRL"));
    }
}
