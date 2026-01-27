package com.nttdata.finance_api.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
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
    @Column(nullable = false)
    private TransactionType type;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Category category;

    @Column(name = "date", nullable = false, columnDefinition = "DATE")
    private LocalDate date;

    // 🔹 NOVO — taxa de câmbio
    @Column(name = "exchange_rate")
    private BigDecimal exchangeRate;

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    @JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
    private User user;

    protected Transaction() {}

    // 🔹 Construtor NOVO (com câmbio)
    public Transaction(
            BigDecimal amount,
            TransactionType type,
            Category category,
            LocalDate date,
            User user,
            BigDecimal exchangeRate
    ) {
        this.amount = amount;
        this.type = type;
        this.category = category;
        this.date = date;
        this.user = user;
        this.exchangeRate = exchangeRate;
    }

    // 🔹 Construtor ANTIGO (mantido para não quebrar nada)
    public Transaction(
            BigDecimal amount,
            TransactionType type,
            Category category,
            LocalDate date,
            User user
    ) {
        this(amount, type, category, date, user, null);
    }

    public Long getId() { return id; }
    public BigDecimal getAmount() { return amount; }
    public TransactionType getType() { return type; }
    public Category getCategory() { return category; }
    public LocalDate getDate() { return date; }
    public BigDecimal getExchangeRate() { return exchangeRate; }
    public User getUser() { return user; }
}