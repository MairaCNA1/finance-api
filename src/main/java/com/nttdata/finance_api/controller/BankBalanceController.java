package com.nttdata.finance_api.controller;

import com.nttdata.finance_api.dto.ApiResponse;
import com.nttdata.finance_api.dto.BankBalanceResponse;
import com.nttdata.finance_api.service.BankBalanceService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class BankBalanceController {

    private final BankBalanceService service;

    public BankBalanceController(BankBalanceService service) {
        this.service = service;
    }

    @GetMapping("/balance/{userId}")
    public ResponseEntity<ApiResponse<BankBalanceResponse>> getBalance(
            @PathVariable Long userId) {

        BankBalanceResponse balance = service.getBalanceByUser(userId);

        ApiResponse<BankBalanceResponse> response =
                new ApiResponse<>(
                        200,
                        "Balance retrieved successfully",
                        balance
                );

        return ResponseEntity.ok(response);
    }
}
