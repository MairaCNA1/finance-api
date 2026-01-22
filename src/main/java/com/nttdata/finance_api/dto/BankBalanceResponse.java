package com.nttdata.finance_api.dto;

import java.math.BigDecimal;

public class BankBalanceResponse {

    private final Long userId;
    private final BigDecimal balance;
    private final String currency;

    public BankBalanceResponse(Long userId, BigDecimal balance, String currency) {
        this.userId = userId;
        this.balance = balance;
        this.currency = currency;
    }

    public Long getUserId() {
        return userId;
    }

    public BigDecimal getBalance() {
        return balance;
    }

    public String getCurrency() {
        return currency;
    }
}
