package com.nttdata.finance_api.service;

import com.nttdata.finance_api.domain.Transaction;
import com.nttdata.finance_api.domain.TransactionType;
import com.nttdata.finance_api.dto.ExpenseSummaryDTO;
import com.nttdata.finance_api.exception.ResourceNotFoundException;
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

        List<Transaction> transactions = repository.findByUserId(userId);

        if (transactions.isEmpty()) {
            throw new ResourceNotFoundException(
                    "No transactions found for user id: " + userId
            );
        }

        return transactions;
    }

    // 🔹 Total de despesas por categoria
    public List<ExpenseSummaryDTO> totalByCategory(Long userId) {

        List<ExpenseSummaryDTO> summary =
                repository.totalByCategory(userId, TransactionType.EXPENSE);

        if (summary.isEmpty()) {
            throw new ResourceNotFoundException(
                    "No expense summary by category found for user id: " + userId
            );
        }

        return summary;
    }

    // 🔹 Total de despesas por dia
    public List<ExpenseSummaryDTO> totalByDay(Long userId) {

        List<ExpenseSummaryDTO> summary =
                repository.totalByDay(userId, TransactionType.EXPENSE);

        if (summary.isEmpty()) {
            throw new ResourceNotFoundException(
                    "No expense summary by day found for user id: " + userId
            );
        }

        return summary;
    }
}
