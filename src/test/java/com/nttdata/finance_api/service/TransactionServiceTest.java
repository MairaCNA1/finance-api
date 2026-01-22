package com.nttdata.finance_api.service;

import com.nttdata.finance_api.domain.Transaction;
import com.nttdata.finance_api.dto.ExpenseSummaryDTO;
import com.nttdata.finance_api.exception.ResourceNotFoundException;
import com.nttdata.finance_api.repository.TransactionRepository;
import com.nttdata.finance_api.domain.TransactionType;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TransactionServiceTest {

    @Mock
    private TransactionRepository repository;

    @InjectMocks
    private TransactionService service;

    @Test
    void shouldReturnTransactionsWhenUserHasTransactions() {
        Transaction transaction = new Transaction();

        when(repository.findByUserId(1L))
                .thenReturn(List.of(transaction));

        List<Transaction> result = service.findByUser(1L);

        assertFalse(result.isEmpty());
        verify(repository).findByUserId(1L);
    }

    @Test
    void shouldThrowExceptionWhenUserHasNoTransactions() {
        when(repository.findByUserId(1L))
                .thenReturn(List.of());

        assertThrows(
                ResourceNotFoundException.class,
                () -> service.findByUser(1L)
        );

        verify(repository).findByUserId(1L);
    }

    @Test
    void shouldReturnExpenseSummaryByCategory() {
        ExpenseSummaryDTO summary =
                new ExpenseSummaryDTO("FOOD", new BigDecimal("100.00"));

        when(repository.totalByCategory(1L, TransactionType.EXPENSE))
                .thenReturn(List.of(summary));

        List<ExpenseSummaryDTO> result =
                service.totalByCategory(1L);

        assertEquals(1, result.size());
        verify(repository)
                .totalByCategory(1L, TransactionType.EXPENSE);
    }
}
