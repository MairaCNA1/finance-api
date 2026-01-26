package com.nttdata.finance_api.controller;

import com.nttdata.finance_api.domain.Transaction;
import com.nttdata.finance_api.dto.*;
import com.nttdata.finance_api.service.TransactionService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/transactions")
public class TransactionController {

    private final TransactionService service;

    public TransactionController(TransactionService service) {
        this.service = service;
    }

    // 🔒 USER cria transação apenas para si
    @PreAuthorize("hasRole('USER')")
    @PostMapping
    public ResponseEntity<ApiResponse<Transaction>> create(
            @RequestBody @Valid CreateTransactionRequest request) {

        Transaction created = service.create(request);

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(new ApiResponse<>(
                        201,
                        "Transação criada",
                        created
                ));
    }

    // 🔒 USER vê SOMENTE as próprias transações
    @PreAuthorize("@userSecurity.isOwner(#userId)")
    @GetMapping("/user/{userId}")
    public ResponseEntity<ApiResponse<List<Transaction>>> listByUser(
            @PathVariable Long userId) {

        return ResponseEntity.ok(
                new ApiResponse<>(
                        200,
                        "Transações encontradas",
                        service.findByUser(userId)
                )
        );
    }

    @PreAuthorize("@userSecurity.isOwner(#userId)")
    @GetMapping("/analysis/category/{userId}")
    public ResponseEntity<ApiResponse<List<ExpenseSummaryDTO>>> totalByCategory(
            @PathVariable Long userId) {

        return ResponseEntity.ok(
                new ApiResponse<>(
                        200,
                        "Resumo por categoria",
                        service.totalByCategory(userId)
                )
        );
    }

    @PreAuthorize("@userSecurity.isOwner(#userId)")
    @GetMapping("/analysis/day/{userId}")
    public ResponseEntity<ApiResponse<List<ExpenseSummaryDTO>>> totalByDay(
            @PathVariable Long userId) {

        return ResponseEntity.ok(
                new ApiResponse<>(
                        200,
                        "Resumo por dia",
                        service.totalByDay(userId)
                )
        );
    }

    @PreAuthorize("@userSecurity.isOwner(#userId)")
    @GetMapping("/analysis/month/{userId}")
    public ResponseEntity<ApiResponse<List<ExpenseSummaryDTO>>> totalByMonth(
            @PathVariable Long userId) {

        return ResponseEntity.ok(
                new ApiResponse<>(
                        200,
                        "Resumo por mês",
                        service.totalByMonth(userId)
                )
        );
    }

    // 🔒 TRANSFERÊNCIA:
    // remetente é SEMPRE o usuário logado (validado no service)
    @PreAuthorize("hasRole('USER')")
    @PostMapping("/transfer")
    public ResponseEntity<ApiResponse<Void>> transfer(
            @RequestBody @Valid CreateTransferRequest request) {

        service.transfer(request);

        return ResponseEntity.ok(
                new ApiResponse<>(
                        200,
                        "Transferência realizada",
                        null
                )
        );
    }
}