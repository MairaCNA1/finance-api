package com.nttdata.finance_api.service;

import com.nttdata.finance_api.dto.BankBalanceResponse;
import com.nttdata.finance_api.exception.ResourceNotFoundException;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
public class BankBalanceService {

    private final TransactionService transactionService;

    public BankBalanceService(TransactionService transactionService) {
        this.transactionService = transactionService;
    }

    public BankBalanceResponse getBalanceByUser(Long userId) {

        BigDecimal balance = transactionService.calculateBalance(userId);

        if (balance == null) {
            throw new ResourceNotFoundException(
                    "Bank balance not found for user id: " + userId
            );
        }

        return new BankBalanceResponse(
                userId,
                balance,
                "BRL"
        );
    }
}