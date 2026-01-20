package com.nttdata.finance_api.controller;

import com.nttdata.finance_api.domain.Transaction;
import com.nttdata.finance_api.service.TransactionService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/transactions")
public class TransactionController {

    private final TransactionService service;

    public TransactionController(TransactionService service) {
        this.service = service;
    }

    @PostMapping
    public Transaction create(@RequestBody Transaction transaction) {
        return service.create(transaction);
    }

    @GetMapping("/user/{userId}")
    public List<Transaction> listByUser(@PathVariable Long userId) {
        return service.findByUser(userId);
    }
}
