package com.nttdata.finance_api.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.nttdata.finance_api.domain.Category;
import com.nttdata.finance_api.domain.Transaction;
import com.nttdata.finance_api.domain.TransactionType;
import com.nttdata.finance_api.domain.User;
import com.nttdata.finance_api.dto.ExpenseSummaryDTO;
import com.nttdata.finance_api.service.TransactionService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(TransactionController.class)
class TransactionControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private TransactionService transactionService;

    @Autowired
    private ObjectMapper objectMapper;

    private User mockUser() {
        return new User("Nebulosa", "nebulosa@email.com");
    }

    @Test
    void shouldCreateTransaction() throws Exception {

        Transaction transaction = new Transaction(
                BigDecimal.valueOf(100),
                TransactionType.EXPENSE,
                Category.FOOD,
                LocalDate.now(),
                mockUser()
        );

        when(transactionService.create(any(Transaction.class)))
                .thenReturn(transaction);

        mockMvc.perform(post("/transactions")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(transaction)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.data.amount").value(100))
                .andExpect(jsonPath("$.data.category").value("FOOD"));
    }

    @Test
    void shouldListTransactionsByUser() throws Exception {

        Transaction transaction = new Transaction(
                BigDecimal.valueOf(50),
                TransactionType.EXPENSE,
                Category.TRANSPORT,
                LocalDate.now(),
                mockUser()
        );

        when(transactionService.findByUser(1L))
                .thenReturn(List.of(transaction));

        mockMvc.perform(get("/transactions/user/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data[0].category").value("TRANSPORT"));
    }

    @Test
    void shouldReturnExpenseSummaryByCategory() throws Exception {

        ExpenseSummaryDTO summary =
                new ExpenseSummaryDTO("FOOD", BigDecimal.valueOf(200));

        when(transactionService.totalByCategory(1L))
                .thenReturn(List.of(summary));

        mockMvc.perform(get("/transactions/analysis/category/1"))
                .andExpect(status().isOk())
                // 🔥 AQUI ESTÁ A CORREÇÃO
                .andExpect(jsonPath("$.data[0].label").value("FOOD"))
                .andExpect(jsonPath("$.data[0].total").value(200));
    }

    @Test
    void shouldReturnExpenseSummaryByDay() throws Exception {

        ExpenseSummaryDTO summary =
                new ExpenseSummaryDTO("2025-01-20", BigDecimal.valueOf(150));

        when(transactionService.totalByDay(1L))
                .thenReturn(List.of(summary));

        mockMvc.perform(get("/transactions/analysis/day/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data[0].label").value("2025-01-20"))
                .andExpect(jsonPath("$.data[0].total").value(150));
    }
}
