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


    @Query("""
    SELECT new com.nttdata.finance_api.dto.ExpenseSummaryDTO(
        CAST(t.date AS java.time.LocalDate),
        SUM(t.amount)
    )
    FROM Transaction t
    WHERE t.type = :type
      AND t.user.id = :userId
    GROUP BY CAST(t.date AS java.time.LocalDate)
    ORDER BY CAST(t.date AS java.time.LocalDate)
""")
    List<ExpenseSummaryDTO> totalByDay(
            @Param("userId") Long userId,
            @Param("type") TransactionType type
    );


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


    @Query("""
        SELECT COALESCE(SUM(t.amount), 0)
        FROM Transaction t
        WHERE t.user.id = :userId
          AND t.category IN ('DEPOSIT', 'SALARY', 'TRANSFER_IN')
    """)
    BigDecimal sumIncomeForBalance(@Param("userId") Long userId);


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