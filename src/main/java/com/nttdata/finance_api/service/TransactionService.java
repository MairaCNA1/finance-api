package com.nttdata.finance_api.service;

import com.nttdata.finance_api.domain.Transaction;
import com.nttdata.finance_api.repository.TransactionRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TransactionService {

    private final TransactionRepository repository;

    public TransactionService(TransactionRepository repository) {
        this.repository = repository;
    }

    public Transaction create(Transaction transaction) {
        return repository.save(transaction);
    }

    public List<Transaction> findByUser(Long userId) {
        return repository.findByUserId(userId);
    }
}
