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
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class TransactionServiceTest {

    private static final UUID ACCOUNT_ID = UUID.randomUUID();
    private static final UUID ACCOUNT_ID_OUT = UUID.randomUUID();
    private static final BigDecimal AMOUNT = BigDecimal.TEN;
    private static final String CURRENCY = "USD";
    private static final TransactionDirection DIRECTION = TransactionDirection.IN;
    private static final String DESCRIPTION = "Some Mock description";

    @Mock
    private TransactionValidation validation;
    @Mock
    private AccountService accountService;
    @Mock
    private TransactionRepository transactionRepository;
    @Mock
    private BalanceRepository balanceRepository;

    @InjectMocks
    private TransactionService service;

    @Test
    void createTransaction_success() {
        var request = TransactionCreateRequest.builder()
                .accountId(ACCOUNT_ID)
                .amount(AMOUNT)
                .currency(CURRENCY)
                .direction(DIRECTION)
                .description(DESCRIPTION)
                .build();
        var balance = Balance.builder()
                .id(10)
                .currency(CURRENCY)
                .amount(AMOUNT)
                .build();
        var account = Account.builder()
                .id(UUID.randomUUID())
                .customerId(UUID.randomUUID())
                .balances(List.of(balance))
                .build();
        when(accountService.getAccountById((String) any())).thenReturn(account);

        var transaction = service.createTransaction(request);

        assertThat(transaction).isNotNull()
                .isInstanceOf(Transaction.class)
                .extracting("accountId")
                .isEqualTo(ACCOUNT_ID);
    }

    @Test
    void createInternalTransaction_success() {
        var request = InternalTransactionCreateRequest.builder()
                .accountIn(ACCOUNT_ID)
                .accountOut(ACCOUNT_ID_OUT)
                .amount(AMOUNT)
                .currency(CURRENCY)
                .description(DESCRIPTION)
                .build();
        var balance = Balance.builder()
                .id(10)
                .currency(CURRENCY)
                .amount(AMOUNT)
                .build();
        var account = Account.builder()
                .id(UUID.randomUUID())
                .customerId(UUID.randomUUID())
                .balances(List.of(balance))
                .build();
        when(accountService.getAccountById((String) any())).thenReturn(account);

        var transactions = service.createInternalTransaction(request);

        assertThat(transactions).isNotNull()
                .extracting(s -> s.getAccountId())
                .contains(ACCOUNT_ID, ACCOUNT_ID_OUT);
    }

    @Test
    void createTransaction_balanceForCurrencyNotFound() {
        var request = TransactionCreateRequest.builder()
                .accountId(ACCOUNT_ID)
                .amount(AMOUNT)
                .currency(CURRENCY)
                .direction(DIRECTION)
                .description(DESCRIPTION)
                .build();

        var balance = Balance.builder()
                .id(10)
                .currency("EUR")
                .amount(AMOUNT)
                .build();
        var account = Account.builder()
                .id(UUID.randomUUID())
                .customerId(UUID.randomUUID())
                .balances(List.of(balance))
                .build();
        when(accountService.getAccountById((String) any())).thenReturn(account);

        assertThatThrownBy(() -> service.createTransaction(request)).isInstanceOf(TransactionCreateError.class);
    }

    @Test
    void getTransaction_success() {
        var uid = UUID.randomUUID();
        var transaction = Transaction.builder()
                .accountId(ACCOUNT_ID)
                .transactionId(uid)
                .amount(AMOUNT)
                .currency(CURRENCY)
                .direction(DIRECTION.toString())
                .description(DESCRIPTION)
                .build();
        when(transactionRepository.getTransactionById(uid))
                .thenReturn(Optional.of(transaction));

        var curTransaction = service.getTransaction(uid.toString());

        assertThat(curTransaction)
                .isNotNull()
                .isEqualTo(transaction);
    }

    @Test
    void getTransaction_notFound() {
        var uid = UUID.randomUUID();
        when(transactionRepository.getTransactionById(uid))
                .thenReturn(Optional.empty());

        assertThatThrownBy(() -> service.getTransaction(uid.toString()))
                .isInstanceOf(AccountNotFoundException.class);
    }

}