package com.nttdata.finance_api.config.kafka.event;

import java.math.BigDecimal;

public class TransactionCreatedEvent {

    private Long userId;
    private BigDecimal amount;
    private String category;

    public TransactionCreatedEvent(
            Long userId,
            BigDecimal amount,
            String category
    ) {
        this.userId = userId;
        this.amount = amount;
        this.category = category;
    }

    public Long getUserId() {
        return userId;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public String getCategory() {
        return category;
    }
}