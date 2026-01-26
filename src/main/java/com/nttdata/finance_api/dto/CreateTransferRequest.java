package com.nttdata.finance_api.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.math.BigDecimal;
import java.time.LocalDate;

public record CreateTransferRequest(

        @NotNull
        Long fromUserId,

        @NotNull
        Long toUserId,

        @NotNull
        @Positive
        BigDecimal amount,

        @NotNull
        LocalDate date
) {}
