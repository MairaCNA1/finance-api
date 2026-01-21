package com.nttdata.finance_api.dto;

import java.math.BigDecimal;

public class BankBalanceResponse {

    private Long userId;
    private BigDecimal balance;
    private String currency;

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
