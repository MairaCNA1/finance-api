package com.nttdata.finance_api.repository;

import com.nttdata.finance_api.domain.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TransactionRepository extends JpaRepository<Transaction, Long> {

    List<Transaction> findByUserId(Long userId);
}
