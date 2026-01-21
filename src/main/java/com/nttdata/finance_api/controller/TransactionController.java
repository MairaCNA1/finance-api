package com.nttdata.finance_api.controller;

import com.nttdata.finance_api.domain.Transaction;
import com.nttdata.finance_api.dto.ApiResponse;
import com.nttdata.finance_api.dto.ExpenseSummaryDTO;
import com.nttdata.finance_api.service.TransactionService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/transactions")
public class TransactionController {

    private final TransactionService service;

    public TransactionController(TransactionService service) {
        this.service = service;
    }

    // 1️⃣ Criar transação
    @PostMapping
    public ResponseEntity<ApiResponse<Transaction>> create(
            @RequestBody @Valid Transaction transaction) {

        Transaction createdTransaction = service.create(transaction);

        ApiResponse<Transaction> response =
                new ApiResponse<>(
                        201,
                        "Transaction created successfully",
                        createdTransaction
                );

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    // 2️⃣ Listar transações por usuário
    @GetMapping("/user/{userId}")
    public ResponseEntity<ApiResponse<List<Transaction>>> listByUser(
            @PathVariable Long userId) {

        List<Transaction> transactions = service.findByUser(userId);

        ApiResponse<List<Transaction>> response =
                new ApiResponse<>(
                        200,
                        "Transactions retrieved successfully",
                        transactions
                );

        return ResponseEntity.ok(response);
    }

    // 3️⃣ Análise de despesas por categoria
    @GetMapping("/analysis/category/{userId}")
    public ResponseEntity<ApiResponse<List<ExpenseSummaryDTO>>> totalByCategory(
            @PathVariable Long userId) {

        List<ExpenseSummaryDTO> summary = service.totalByCategory(userId);

        ApiResponse<List<ExpenseSummaryDTO>> response =
                new ApiResponse<>(
                        200,
                        "Expense summary by category retrieved successfully",
                        summary
                );

        return ResponseEntity.ok(response);
    }

    // 4️⃣ Análise de despesas por dia
    @GetMapping("/analysis/day/{userId}")
    public ResponseEntity<ApiResponse<List<ExpenseSummaryDTO>>> totalByDay(
            @PathVariable Long userId) {

        List<ExpenseSummaryDTO> summary = service.totalByDay(userId);

        ApiResponse<List<ExpenseSummaryDTO>> response =
                new ApiResponse<>(
                        200,
                        "Expense summary by day retrieved successfully",
                        summary
                );

        return ResponseEntity.ok(response);
    }
}
