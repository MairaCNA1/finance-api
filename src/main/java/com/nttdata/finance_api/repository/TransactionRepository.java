package com.nttdata.finance_api.repository;

import com.nttdata.finance_api.domain.Transaction;
import com.nttdata.finance_api.domain.TransactionType;
import com.nttdata.finance_api.dto.ExpenseSummaryDTO;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.math.BigDecimal;
import java.util.List;

public interface TransactionRepository extends JpaRepository<Transaction, Long> {

    List<Transaction> findByUser_Id(Long userId);

    // 📊 Total por categoria (análise)
    @Query("""
        SELECT new com.nttdata.finance_api.dto.ExpenseSummaryDTO(
            t.category, SUM(t.amount)
        )
        FROM Transaction t
        WHERE t.type = :type
          AND t.user.id = :userId
        GROUP BY t.category
    """)
    List<ExpenseSummaryDTO> totalByCategory(
            @Param("userId") Long userId,
            @Param("type") TransactionType type
    );

    // 📊 Total por dia
    @Query("""
        SELECT new com.nttdata.finance_api.dto.ExpenseSummaryDTO(
            t.date, SUM(t.amount)
        )
        FROM Transaction t
        WHERE t.type = :type
          AND t.user.id = :userId
        GROUP BY t.date
    """)
    List<ExpenseSummaryDTO> totalByDay(
            @Param("userId") Long userId,
            @Param("type") TransactionType type
    );

    // 📊 Total por mês
    @Query("""
        SELECT new com.nttdata.finance_api.dto.ExpenseSummaryDTO(
            FUNCTION('to_char', t.date, 'YYYY-MM'),
            SUM(t.amount)
        )
        FROM Transaction t
        WHERE t.type = :type
          AND t.user.id = :userId
        GROUP BY FUNCTION('to_char', t.date, 'YYYY-MM')
        ORDER BY FUNCTION('to_char', t.date, 'YYYY-MM')
    """)
    List<ExpenseSummaryDTO> totalByMonth(
            @Param("userId") Long userId,
            @Param("type") TransactionType type
    );

    // 🔧 Mantido para compatibilidade (outros testes usam)
    @Query("""
        SELECT COALESCE(SUM(t.amount), 0)
        FROM Transaction t
        WHERE t.user.id = :userId
          AND t.type = :type
    """)
    BigDecimal sumAmountByType(
            @Param("userId") Long userId,
            @Param("type") TransactionType type
    );

    // ✅ NOVO — saldo real (entradas válidas)
    @Query("""
        SELECT COALESCE(SUM(t.amount), 0)
        FROM Transaction t
        WHERE t.user.id = :userId
          AND t.category IN ('DEPOSIT', 'SALARY', 'TRANSFER_IN')
    """)
    BigDecimal sumIncomeForBalance(@Param("userId") Long userId);

    // ✅ NOVO — saldo real (saídas válidas)
    @Query("""
        SELECT COALESCE(SUM(t.amount), 0)
        FROM Transaction t
        WHERE t.user.id = :userId
          AND t.category IN (
            'WITHDRAW',
            'TRANSFER_OUT',
            'FOOD',
            'RENT',
            'TRANSPORT',
            'HEALTH',
            'EDUCATION',
            'OTHER'
          )
    """)
    BigDecimal sumExpenseForBalance(@Param("userId") Long userId);
}