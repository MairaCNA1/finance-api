package com.nttdata.finance_api.service;

import com.nttdata.finance_api.domain.*;
import com.nttdata.finance_api.dto.CreateTransactionRequest;
import com.nttdata.finance_api.dto.ExpenseSummaryDTO;
import com.nttdata.finance_api.exception.ResourceNotFoundException;
import com.nttdata.finance_api.repository.TransactionRepository;
import com.nttdata.finance_api.repository.UserRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TransactionServiceTest {

    @Mock
    private TransactionRepository transactionRepository;

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private TransactionService transactionService;

    // =======================
    // 🔐 SECURITY MOCK
    // =======================
    private void mockAuthenticatedUser(String email) {
        Authentication auth = mock(Authentication.class);
        when(auth.isAuthenticated()).thenReturn(true);
        when(auth.getName()).thenReturn(email);

        SecurityContext context = mock(SecurityContext.class);
        when(context.getAuthentication()).thenReturn(auth);

        SecurityContextHolder.setContext(context);
    }

    @AfterEach
    void clearSecurityContext() {
        SecurityContextHolder.clearContext();
    }

    // =======================
    // 🧪 HELPERS
    // =======================
    private User mockUser(Long id, String email) {
        User user = new User(
                "Nebulosa",
                email,
                "senhaFake",
                Role.USER
        );
        try {
            var field = User.class.getDeclaredField("id");
            field.setAccessible(true);
            field.set(user, id);
        } catch (Exception ignored) {}
        return user;
    }

    // =======================
    // ✅ CREATE TRANSACTION
    // =======================
    @Test
    void shouldCreateTransactionSuccessfully() {
        mockAuthenticatedUser("nebulosa@email.com");

        User user = mockUser(1L, "nebulosa@email.com");

        CreateTransactionRequest request =
                new CreateTransactionRequest(
                        BigDecimal.valueOf(100),
                        TransactionType.EXPENSE,
                        Category.FOOD,
                        LocalDate.now(),
                        1L
                );

        when(userRepository.findByEmail("nebulosa@email.com"))
                .thenReturn(Optional.of(user));

        when(transactionRepository.save(any(Transaction.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        Transaction result = transactionService.create(request);

        assertNotNull(result);
        assertEquals(BigDecimal.valueOf(100), result.getAmount());
        assertEquals(Category.FOOD, result.getCategory());
        assertEquals(user, result.getUser());

        verify(transactionRepository).save(any(Transaction.class));
    }

    // =======================
    // ❌ USER NOT FOUND
    // =======================
    @Test
    void shouldThrowExceptionWhenUserNotFoundOnCreate() {
        mockAuthenticatedUser("nebulosa@email.com");

        CreateTransactionRequest request =
                new CreateTransactionRequest(
                        BigDecimal.valueOf(50),
                        TransactionType.EXPENSE,
                        Category.TRANSPORT,
                        LocalDate.now(),
                        1L
                );

        when(userRepository.findByEmail("nebulosa@email.com"))
                .thenReturn(Optional.empty());

        assertThrows(
                ResourceNotFoundException.class,
                () -> transactionService.create(request)
        );

        verify(transactionRepository, never()).save(any());
    }

    // =======================
    // ✅ WITHDRAW COM SALDO
    // =======================
    @Test
    void shouldAllowWithdrawWhenBalanceIsSufficient() {
        mockAuthenticatedUser("nebulosa@email.com");

        User user = mockUser(1L, "nebulosa@email.com");

        CreateTransactionRequest request =
                new CreateTransactionRequest(
                        BigDecimal.valueOf(150),
                        TransactionType.EXPENSE,
                        Category.WITHDRAW,
                        LocalDate.now(),
                        1L
                );

        when(userRepository.findByEmail("nebulosa@email.com"))
                .thenReturn(Optional.of(user));

        when(transactionRepository.sumAmountByType(1L, TransactionType.INCOME))
                .thenReturn(BigDecimal.valueOf(500));

        when(transactionRepository.sumAmountByType(1L, TransactionType.EXPENSE))
                .thenReturn(BigDecimal.valueOf(100));

        when(transactionRepository.save(any(Transaction.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        Transaction result = transactionService.create(request);

        assertNotNull(result);
        assertEquals(Category.WITHDRAW, result.getCategory());
        assertEquals(BigDecimal.valueOf(150), result.getAmount());
    }

    // =======================
    // ✅ LIST TRANSACTIONS
    // =======================
    @Test
    void shouldReturnTransactionsByUser() {
        mockAuthenticatedUser("nebulosa@email.com");

        User user = mockUser(1L, "nebulosa@email.com");

        Transaction transaction = new Transaction(
                BigDecimal.valueOf(80),
                TransactionType.EXPENSE,
                Category.HEALTH,
                LocalDate.now(),
                user
        );

        when(userRepository.findByEmail("nebulosa@email.com"))
                .thenReturn(Optional.of(user));

        when(transactionRepository.findByUser_Id(1L))
                .thenReturn(List.of(transaction));

        List<Transaction> result = transactionService.findByUser(1L);

        assertEquals(1, result.size());
        assertEquals(Category.HEALTH, result.get(0).getCategory());
    }

    // =======================
    // ❌ NO TRANSACTIONS
    // =======================
    @Test
    void shouldThrowExceptionWhenNoTransactionsFound() {
        mockAuthenticatedUser("nebulosa@email.com");

        User user = mockUser(1L, "nebulosa@email.com");

        when(userRepository.findByEmail("nebulosa@email.com"))
                .thenReturn(Optional.of(user));

        when(transactionRepository.findByUser_Id(1L))
                .thenReturn(List.of());

        assertThrows(
                ResourceNotFoundException.class,
                () -> transactionService.findByUser(1L)
        );
    }

    // =======================
    // ✅ EXPENSE SUMMARY
    // =======================
    @Test
    void shouldReturnExpenseSummaryByMonth() {
        mockAuthenticatedUser("nebulosa@email.com");

        User user = mockUser(1L, "nebulosa@email.com");

        when(userRepository.findByEmail("nebulosa@email.com"))
                .thenReturn(Optional.of(user));

        ExpenseSummaryDTO summary =
                new ExpenseSummaryDTO("2026-01", BigDecimal.valueOf(450));

        when(transactionRepository.totalByMonth(1L, TransactionType.EXPENSE))
                .thenReturn(List.of(summary));

        List<ExpenseSummaryDTO> result =
                transactionService.totalByMonth(1L);

        assertEquals(1, result.size());
        assertEquals("2026-01", result.get(0).getLabel());
        assertEquals(BigDecimal.valueOf(450), result.get(0).getTotal());
    }
}