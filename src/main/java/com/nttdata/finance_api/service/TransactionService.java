package com.nttdata.finance_api.service;

import com.nttdata.finance_api.config.kafka.event.TransactionCreatedEvent;
import com.nttdata.finance_api.config.kafka.producer.TransactionEventProducer;
import com.nttdata.finance_api.config.security.SecurityUtils;
import com.nttdata.finance_api.domain.Category;
import com.nttdata.finance_api.domain.Transaction;
import com.nttdata.finance_api.domain.TransactionType;
import com.nttdata.finance_api.domain.User;
import com.nttdata.finance_api.dto.CreateTransactionRequest;
import com.nttdata.finance_api.dto.CreateTransferRequest;
import com.nttdata.finance_api.dto.ExpenseSummaryDTO;
import com.nttdata.finance_api.exception.ResourceNotFoundException;
import com.nttdata.finance_api.repository.TransactionRepository;
import com.nttdata.finance_api.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

@Service
public class TransactionService {

    private static final Logger log =
            LoggerFactory.getLogger(TransactionService.class);

    private final TransactionRepository transactionRepository;
    private final UserRepository userRepository;
    private final TransactionEventProducer transactionEventProducer;

    public TransactionService(
            TransactionRepository transactionRepository,
            UserRepository userRepository,
            TransactionEventProducer transactionEventProducer
    ) {
        this.transactionRepository = transactionRepository;
        this.userRepository = userRepository;
        this.transactionEventProducer = transactionEventProducer;
    }

    /**
     * 🔒 Criação de transação
     * Usuário SEMPRE vem do JWT
     */
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

        // 🔔 Kafka event (fire-and-forget)
        try {
            transactionEventProducer.send(
                    new TransactionCreatedEvent(
                            loggedUser.getId(),
                            savedTransaction.getAmount(),
                            savedTransaction.getCategory().name()
                    )
            );
        } catch (Exception ex) {
            log.warn(
                    "Kafka unavailable. Transaction event not sent. userId={}",
                    loggedUser.getId(),
                    ex
            );
        }

        return savedTransaction;
    }

    /**
     * 🔒 Lista apenas transações do usuário logado
     */
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

        List<ExpenseSummaryDTO> summary =
                transactionRepository.totalByCategory(
                        userId,
                        TransactionType.EXPENSE
                );

        if (summary.isEmpty()) {
            throw new ResourceNotFoundException(
                    "No expense summary by category found for user id: " + userId
            );
        }

        return summary;
    }

    public List<ExpenseSummaryDTO> totalByDay(Long userId) {
        validateOwnership(userId);

        List<ExpenseSummaryDTO> summary =
                transactionRepository.totalByDay(
                        userId,
                        TransactionType.EXPENSE
                );

        if (summary.isEmpty()) {
            throw new ResourceNotFoundException(
                    "No expense summary by day found for user id: " + userId
            );
        }

        return summary;
    }

    public List<ExpenseSummaryDTO> totalByMonth(Long userId) {
        validateOwnership(userId);

        List<ExpenseSummaryDTO> summary =
                transactionRepository.totalByMonth(
                        userId,
                        TransactionType.EXPENSE
                );

        if (summary.isEmpty()) {
            throw new ResourceNotFoundException(
                    "No expense summary by month found for user id: " + userId
            );
        }

        return summary;
    }

    /**
     * 🔒 Transferência
     * Remetente = usuário logado
     */
    public void transfer(CreateTransferRequest request) {

        User fromUser = getLoggedUser();

        User toUser = userRepository.findById(request.toUserId())
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Receiver not found with id: "
                                        + request.toUserId()
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

    // 💰 Saldo consolidado (REGRA CORRETA)
    public BigDecimal calculateBalance(Long userId) {

        BigDecimal income =
                transactionRepository.sumIncomeForBalance(userId);

        BigDecimal expense =
                transactionRepository.sumExpenseForBalance(userId);

        return income.subtract(expense);
    }

    // ============================
    // 🔐 MÉTODOS PRIVADOS
    // ============================

    private User getLoggedUser() {

        String email = SecurityUtils.getLoggedUserEmail();

        if (email == null) {
            throw new IllegalStateException("User not authenticated");
        }

        return userRepository.findByEmail(email)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Logged user not found"
                        )
                );
    }

    private void validateSufficientBalance(
            Long userId,
            BigDecimal amount
    ) {

        BigDecimal balance = calculateBalance(userId);

        if (balance.compareTo(amount) < 0) {
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
