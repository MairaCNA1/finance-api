package com.nttdata.finance_api.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.nttdata.finance_api.domain.Category;
import com.nttdata.finance_api.domain.TransactionType;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.math.BigDecimal;
import java.time.LocalDate;

public record CreateTransactionRequest(

        @NotNull
        @Positive
        BigDecimal amount,

        @NotNull
        TransactionType type,

        @NotNull
        Category category,

        @NotNull
        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
        LocalDate date,

        @NotNull
        Long userId)
{}
