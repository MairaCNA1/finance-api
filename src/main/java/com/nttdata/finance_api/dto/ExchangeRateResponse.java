package com.nttdata.finance_api.dto;

import java.math.BigDecimal;
import java.time.LocalDate;

public class ExchangeRateResponse {

    private String currency;
    private LocalDate date;
    private BigDecimal rate;

    public ExchangeRateResponse(String currency, LocalDate date, BigDecimal rate) {
        this.currency = currency;
        this.date = date;
        this.rate = rate;
    }

    public String getCurrency() {
        return currency;
    }

    public LocalDate getDate() {
        return date;
    }

    public BigDecimal getRate() {
        return rate;
    }
}
