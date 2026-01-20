package com.nttdata.finance_api.domain;

import jakarta.persistence.*;

import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Table(name = "transactions")
public class Transaction {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private BigDecimal amount;

    @Enumerated(EnumType.STRING)
    private TransactionType type;

    @Enumerated(EnumType.STRING)
    private Category category;

    private LocalDate date;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    public Transaction() {}

    public Transaction(BigDecimal amount, TransactionType type, Category category, LocalDate date, User user) {
        this.amount = amount;
        this.type = type;
        this.category = category;
        this.date = date;
        this.user = user;
    }

    public Long getId() {
        return id;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public TransactionType getType() {
        return type;
    }

    public Category getCategory() {
        return category;
    }

    public LocalDate getDate() {
        return date;
    }

    public User getUser() {
        return user;
    }
}
