package com.nttdata.finance_api.controller;

import com.nttdata.finance_api.dto.ExchangeRateResponse;
import com.nttdata.finance_api.service.ExchangeRateService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ExchangeRateController {

    private final ExchangeRateService service;

    public ExchangeRateController(ExchangeRateService service) {
        this.service = service;
    }

    @GetMapping("/exchange-rate/{currency}")
    public ExchangeRateResponse getExchangeRate(@PathVariable String currency) {
        return service.getExchangeRate(currency);
    }
}
