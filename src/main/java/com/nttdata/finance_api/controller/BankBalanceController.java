package com.nttdata.finance_api.controller;

import com.nttdata.finance_api.dto.ApiResponse;
import com.nttdata.finance_api.dto.BankBalanceResponse;
import com.nttdata.finance_api.service.BankBalanceService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/balance")
public class BankBalanceController {

    private final BankBalanceService service;

    public BankBalanceController(BankBalanceService service) {
        this.service = service;
    }

    @GetMapping("/{userId}")
    public ResponseEntity<ApiResponse<BankBalanceResponse>> getBalance(
            @PathVariable Long userId) {

        BankBalanceResponse balance = service.getBalanceByUser(userId);

        ApiResponse<BankBalanceResponse> response =
                new ApiResponse<>(
                        200,
                        "Bank balance retrieved successfully",
                        balance
                );

        return ResponseEntity.ok(response);
    }
}
