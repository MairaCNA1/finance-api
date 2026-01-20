package com.nttdata.finance_api.service;

import com.nttdata.finance_api.domain.Transaction;
import com.nttdata.finance_api.domain.TransactionType;
import com.nttdata.finance_api.dto.ExpenseSummaryDTO;
import com.nttdata.finance_api.repository.TransactionRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TransactionService {

    private final TransactionRepository repository;

    public TransactionService(TransactionRepository repository) {
        this.repository = repository;
    }

    // 🔹 Criar transação
    public Transaction create(Transaction transaction) {
        return repository.save(transaction);
    }

    // 🔹 Listar transações por usuário
    public List<Transaction> findByUser(Long userId) {
        return repository.findByUserId(userId);
    }

    // 🔹 Total de despesas por categoria
    public List<ExpenseSummaryDTO> totalByCategory(Long userId) {
        return repository.totalByCategory(userId, TransactionType.EXPENSE);
    }

    // 🔹 Total de despesas por dia
    public List<ExpenseSummaryDTO> totalByDay(Long userId) {
        return repository.totalByDay(userId, TransactionType.EXPENSE);
    }
}
