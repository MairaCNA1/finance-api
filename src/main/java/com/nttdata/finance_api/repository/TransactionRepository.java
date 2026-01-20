package com.nttdata.finance_api.repository;

import com.nttdata.finance_api.domain.Transaction;
import com.nttdata.finance_api.domain.TransactionType;
import com.nttdata.finance_api.dto.ExpenseSummaryDTO;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface TransactionRepository extends JpaRepository<Transaction, Long> {

    List<Transaction> findByUserId(Long userId);

    // 🔹 Total por categoria
    @Query("""
        SELECT new com.nttdata.finance_api.dto.ExpenseSummaryDTO(
            t.category, SUM(t.amount)
        )
        FROM Transaction t
        WHERE t.type = :type AND t.user.id = :userId
        GROUP BY t.category
    """)
    List<ExpenseSummaryDTO> totalByCategory(
            @Param("userId") Long userId,
            @Param("type") TransactionType type
    );

    // 🔹 Total por dia
    @Query("""
        SELECT new com.nttdata.finance_api.dto.ExpenseSummaryDTO(
            t.date, SUM(t.amount)
        )
        FROM Transaction t
        WHERE t.type = :type AND t.user.id = :userId
        GROUP BY t.date
    """)
    List<ExpenseSummaryDTO> totalByDay(
            @Param("userId") Long userId,
            @Param("type") TransactionType type
    );
}
