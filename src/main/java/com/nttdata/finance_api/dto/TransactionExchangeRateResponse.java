package com.nttdata.finance_api.dto;

import java.math.BigDecimal;
import java.time.LocalDate;

public class TransactionExchangeRateResponse {

    private Long transactionId;
    private BigDecimal originalAmount;
    private String originalCurrency;
    private String targetCurrency;
    private BigDecimal exchangeRate;
    private BigDecimal convertedAmount;
    private LocalDate date;

    public TransactionExchangeRateResponse(
            Long transactionId,
            BigDecimal originalAmount,
            String originalCurrency,
            String targetCurrency,
            BigDecimal exchangeRate,
            BigDecimal convertedAmount,
            LocalDate date
    ) {
        this.transactionId = transactionId;
        this.originalAmount = originalAmount;
        this.originalCurrency = originalCurrency;
        this.targetCurrency = targetCurrency;
        this.exchangeRate = exchangeRate;
        this.convertedAmount = convertedAmount;
        this.date = date;
    }

    public Long getTransactionId() {
        return transactionId;
    }

    public BigDecimal getOriginalAmount() {
        return originalAmount;
    }

    public String getOriginalCurrency() {
        return originalCurrency;
    }

    public String getTargetCurrency() {
        return targetCurrency;
    }

    public BigDecimal getExchangeRate() {
        return exchangeRate;
    }

    public BigDecimal getConvertedAmount() {
        return convertedAmount;
    }

    public LocalDate getDate() {
        return date;
    }
}