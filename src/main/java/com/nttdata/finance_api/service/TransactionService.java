package com.nttdata.finance_api.service;

import com.nttdata.finance_api.config.kafka.event.TransactionCreatedEvent;
import com.nttdata.finance_api.config.security.SecurityUtils;
import com.nttdata.finance_api.domain.Category;
import com.nttdata.finance_api.domain.Transaction;
import com.nttdata.finance_api.domain.TransactionType;
import com.nttdata.finance_api.domain.User;
import com.nttdata.finance_api.dto.CreateTransactionRequest;
import com.nttdata.finance_api.dto.CreateTransferRequest;
import com.nttdata.finance_api.dto.ExpenseSummaryDTO;
import com.nttdata.finance_api.dto.ExchangeRateResponse;
import com.nttdata.finance_api.dto.TransactionExchangeRateResponse;
import com.nttdata.finance_api.exception.ResourceNotFoundException;
import com.nttdata.finance_api.repository.TransactionRepository;
import com.nttdata.finance_api.repository.UserRepository;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;

@Service
public class TransactionService {

    private final TransactionRepository transactionRepository;
    private final UserRepository userRepository;
    private final ExchangeRateService exchangeRateService;
    private final ApplicationEventPublisher eventPublisher;

    public TransactionService(
            TransactionRepository transactionRepository,
            UserRepository userRepository,
            ExchangeRateService exchangeRateService,
            ApplicationEventPublisher eventPublisher
    ) {
        this.transactionRepository = transactionRepository;
        this.userRepository = userRepository;
        this.exchangeRateService = exchangeRateService;
        this.eventPublisher = eventPublisher;
    }

    @Transactional
    public Transaction create(CreateTransactionRequest request) {

        User loggedUser = getLoggedUser();

        if (request.category() == Category.WITHDRAW
                && request.type() != TransactionType.EXPENSE) {
            throw new IllegalArgumentException(
                    "Withdraw must be an EXPENSE transaction"
            );
        }

        if (request.category() == Category.WITHDRAW) {
            validateSufficientBalance(
                    loggedUser.getId(),
                    request.amount()
            );
        }

        Transaction transaction = new Transaction(
                request.amount(),
                request.type(),
                request.category(),
                request.date(),
                loggedUser
        );

        Transaction savedTransaction =
                transactionRepository.save(transaction);


        eventPublisher.publishEvent(
                new TransactionCreatedEvent(
                        loggedUser.getId(),
                        savedTransaction.getAmount(),
                        savedTransaction.getCategory().name()
                )
        );

        return savedTransaction;
    }

    public TransactionExchangeRateResponse createWithExchange(
            CreateTransactionRequest request,
            String targetCurrency
    ) {

        Transaction transaction = create(request);

        ExchangeRateResponse exchange =
                exchangeRateService.getExchangeRate(
                        targetCurrency,
                        transaction.getDate().toString()
                );

        BigDecimal convertedAmount =
                transaction.getAmount()
                        .divide(exchange.getRate(), 2, RoundingMode.HALF_UP);

        return new TransactionExchangeRateResponse(
                transaction.getId(),
                transaction.getAmount(),
                "BRL",
                targetCurrency,
                exchange.getRate(),
                convertedAmount,
                transaction.getDate()
        );
    }

    @Transactional
    public void transfer(CreateTransferRequest request) {

        User fromUser = getLoggedUser();

        User toUser = userRepository.findById(request.toUserId())
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Receiver not found with id: " + request.toUserId()
                        )
                );

        validateSufficientBalance(
                fromUser.getId(),
                request.amount()
        );

        Transaction debit = new Transaction(
                request.amount(),
                TransactionType.EXPENSE,
                Category.TRANSFER_OUT,
                request.date(),
                fromUser
        );

        Transaction credit = new Transaction(
                request.amount(),
                TransactionType.INCOME,
                Category.TRANSFER_IN,
                request.date(),
                toUser
        );

        transactionRepository.save(debit);
        transactionRepository.save(credit);
    }

    public TransactionExchangeRateResponse getExchangeRateForTransaction(
            Long transactionId,
            String targetCurrency
    ) {

        User loggedUser = getLoggedUser();

        Transaction transaction = transactionRepository.findById(transactionId)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Transaction not found")
                );

        if (!transaction.getUser().getId().equals(loggedUser.getId())) {
            throw new IllegalArgumentException("Access denied");
        }

        ExchangeRateResponse exchange =
                exchangeRateService.getExchangeRate(
                        targetCurrency,
                        transaction.getDate().toString()
                );

        BigDecimal convertedAmount =
                transaction.getAmount()
                        .divide(exchange.getRate(), 2, RoundingMode.HALF_UP);

        return new TransactionExchangeRateResponse(
                transaction.getId(),
                transaction.getAmount(),
                "BRL",
                targetCurrency,
                exchange.getRate(),
                convertedAmount,
                transaction.getDate()
        );
    }

    public List<Transaction> findByUser(Long userId) {

        User loggedUser = getLoggedUser();

        if (!loggedUser.getId().equals(userId)) {
            throw new IllegalArgumentException("Access denied");
        }

        List<Transaction> transactions =
                transactionRepository.findByUser_Id(userId);

        if (transactions.isEmpty()) {
            throw new ResourceNotFoundException(
                    "No transactions found for user id: " + userId
            );
        }

        return transactions;
    }

    public List<ExpenseSummaryDTO> totalByCategory(Long userId) {
        validateOwnership(userId);
        return transactionRepository.totalByCategory(
                userId,
                TransactionType.EXPENSE
        );
    }

    public List<ExpenseSummaryDTO> totalByDay(Long userId) {
        validateOwnership(userId);
        return transactionRepository.totalByDay(
                userId,
                TransactionType.EXPENSE
        );
    }

    public List<ExpenseSummaryDTO> totalByMonth(Long userId) {
        validateOwnership(userId);
        return transactionRepository.totalByMonth(
                userId,
                TransactionType.EXPENSE
        );
    }

    public BigDecimal calculateBalance(Long userId) {
        BigDecimal income =
                transactionRepository.sumIncomeForBalance(userId);
        BigDecimal expense =
                transactionRepository.sumExpenseForBalance(userId);
        return income.subtract(expense);
    }

    private User getLoggedUser() {

        String email = SecurityUtils.getLoggedUserEmail();

        if (email == null) {
            throw new IllegalStateException("User not authenticated");
        }

        return userRepository.findByEmail(email)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Logged user not found")
                );
    }

    private void validateSufficientBalance(Long userId, BigDecimal amount) {
        if (calculateBalance(userId).compareTo(amount) < 0) {
            throw new IllegalArgumentException("Insufficient balance");
        }
    }

    private void validateOwnership(Long userId) {
        User loggedUser = getLoggedUser();
        if (!loggedUser.getId().equals(userId)) {
            throw new IllegalArgumentException("Access denied");
        }
    }
}