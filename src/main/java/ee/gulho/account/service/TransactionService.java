package ee.gulho.account.service;

import ee.gulho.account.entity.Account;
import ee.gulho.account.entity.Balance;
import ee.gulho.account.entity.Transaction;
import ee.gulho.account.entity.enums.TransactionDirection;
import ee.gulho.account.exception.AccountNotFoundException;
import ee.gulho.account.exception.TransactionCreateError;
import ee.gulho.account.mapper.BalanceRepository;
import ee.gulho.account.mapper.TransactionRepository;
import ee.gulho.account.service.dto.InternalTransactionCreateRequest;
import ee.gulho.account.service.dto.TransactionCreateRequest;
import ee.gulho.account.utils.TransactionValidation;
import lombok.RequiredArgsConstructor;
import org.slf4j.MDC;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.Set;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class TransactionService {

    @Value("${app.sessionId:session_id}")
    public String sessionId;

    private final TransactionValidation validator;
    private final AccountService accountService;
    private final TransactionRepository transactionRepository;
    private final BalanceRepository balanceRepository;

    @Transactional
    public Transaction createTransaction(TransactionCreateRequest request) {
        UUID transactionId = UUID.randomUUID();

        Account account = accountService.getAccountById(request.accountId().toString());
        Balance balance = getBalanceForTransactionCurrency(request, account);
        validator.validate(request, balance);

        BigDecimal newBalanceAmount = doTransaction(request, transactionId, account, balance);

        return createResponse(request, transactionId, newBalanceAmount);
    }

    @Transactional()
    public Set<Transaction> createInternalTransaction(InternalTransactionCreateRequest internalTransaction) {

        var transactionIn = TransactionCreateRequest.builder()
                .accountId(internalTransaction.accountIn())
                .currency(internalTransaction.currency())
                .amount(internalTransaction.amount())
                .direction(TransactionDirection.IN)
                .description(internalTransaction.description())
                .build();
        var transactionOut = TransactionCreateRequest.builder()
                .accountId(internalTransaction.accountOut())
                .currency(internalTransaction.currency())
                .amount(internalTransaction.amount())
                .direction(TransactionDirection.OUT)
                .description(internalTransaction.description())
                .build();
        var inTransactionValue = createTransaction(transactionIn);
        var outTransactionValue = createTransaction(transactionOut);
        return Set.of(inTransactionValue, outTransactionValue);
    }

    public Transaction getTransaction(String transactionId) {
        try {
            UUID uid = UUID.fromString(transactionId);
            return transactionRepository.getTransactionById(uid).orElseThrow(() ->
                    new AccountNotFoundException("Transaction UUID is not found"));
        } catch (IllegalArgumentException ex) {
            throw new AccountNotFoundException("Transaction UUID is incorrect");
        }
    }

    private BigDecimal doTransaction(TransactionCreateRequest transactionCreate, UUID transactionId, Account account, Balance balance) {
        var newBalance = calculateNewAmount(balance.getAmount(), transactionCreate.amount(), transactionCreate.direction());
        balanceRepository.updateBalanceAmount(newBalance, balance.getId(), MDC.get(sessionId));
        transactionRepository.createTransaction(
                transactionId,
                account.getId(),
                transactionCreate.currency(),
                transactionCreate.amount(),
                transactionCreate.direction().toString(),
                transactionCreate.description(),
                MDC.get(sessionId)
        );
        return newBalance;
    }

    private Transaction createResponse(TransactionCreateRequest transactionCreate, UUID transactionId, BigDecimal newBalanceAmount) {
        return Transaction.builder()
                .accountId(transactionCreate.accountId())
                .transactionId(transactionId)
                .amount(transactionCreate.amount())
                .currency(transactionCreate.currency())
                .direction(transactionCreate.direction().toString())
                .description(transactionCreate.description())
                .balanceAmount(newBalanceAmount)
                .build();
    }

    private BigDecimal calculateNewAmount(BigDecimal balanceAmount, BigDecimal transactionAmount, TransactionDirection direction) {
        if (direction.equals(TransactionDirection.IN)) {
            return balanceAmount.add(transactionAmount);
        } else {
            return balanceAmount.subtract(transactionAmount);
        }
    }

    private Balance getBalanceForTransactionCurrency(TransactionCreateRequest request, Account account) {
        var optionalBalance = account.getBalances().stream()
                .filter(balance -> balance.getCurrency().equals(request.currency()))
                .findFirst();
        return optionalBalance.orElseThrow(() ->
                new TransactionCreateError("Account does not have a balance for the current currency"));
    }
}
