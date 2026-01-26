package com.nttdata.finance_api.controller;

import com.nttdata.finance_api.dto.ApiResponse;
import com.nttdata.finance_api.dto.ExchangeRateResponse;
import com.nttdata.finance_api.service.ExchangeRateService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/exchange-rate")
public class ExchangeRateController {

    private final ExchangeRateService service;

    public ExchangeRateController(ExchangeRateService service) {
        this.service = service;
    }

    @GetMapping("/{currency}/{date}")
    public ResponseEntity<ApiResponse<ExchangeRateResponse>> getExchangeRate(
            @PathVariable String currency,
            @PathVariable String date) {

        return ResponseEntity.ok(new ApiResponse<>(
                200,
                "Cotação obtida com sucesso",
                service.getExchangeRate(currency, date)
        ));
    }
}
