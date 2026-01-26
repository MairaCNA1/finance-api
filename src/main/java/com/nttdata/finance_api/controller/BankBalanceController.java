package com.nttdata.finance_api.controller;

import com.nttdata.finance_api.dto.ApiResponse;
import com.nttdata.finance_api.dto.BankBalanceResponse;
import com.nttdata.finance_api.service.BankBalanceService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/balance")
public class BankBalanceController {

    private final BankBalanceService bankBalanceService;

    public BankBalanceController(BankBalanceService bankBalanceService) {
        this.bankBalanceService = bankBalanceService;
    }

    // 🔒 Endpoint autenticado
    @PreAuthorize("hasAnyRole('USER','ADMIN')")
    @GetMapping("/{userId}")
    public ResponseEntity<ApiResponse<BankBalanceResponse>> getBalance(@PathVariable Long userId) {
        BankBalanceResponse balance = bankBalanceService.getBalanceByUser(userId);
        return ResponseEntity.ok(
                new ApiResponse<>(
                        200,
                        "Saldo bancário",
                        balance
                )
        );
    }
}