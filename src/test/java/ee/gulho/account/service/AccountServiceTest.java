package ee.gulho.account.service;

import ee.gulho.account.entity.Account;
import ee.gulho.account.entity.Balance;
import ee.gulho.account.exception.AccountNotFoundException;
import ee.gulho.account.mapper.AccountRepository;
import ee.gulho.account.mapper.BalanceRepository;
import ee.gulho.account.service.dto.AccountCreateRequest;
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
class AccountServiceTest {
    private final String country = "EE";
    private final String eur = "EUR";
    private final String usd = "USD";

    @Mock
    private AccountRepository accountRepository;
    @Mock
    private BalanceRepository balanceRepository;
    @InjectMocks
    private AccountService service;

    @Test
    void createAccount_accountCreated() {
        var id = UUID.randomUUID();
        var request = new AccountCreateRequest(id, country, List.of(eur,usd));
        var account = new Account(UUID.randomUUID(), id, List.of(new Balance(1, BigDecimal.ZERO, "USD")));
        when(accountRepository.getAccountById(any()))
                .thenReturn(Optional.of(account));

        var createdAccount = service.createAccount(request);

        assertThat(createdAccount)
                .isNotNull()
                .isEqualTo(account);
    }

    @Test
    void getAccountById_accountGetOk() {
        var uid = UUID.randomUUID();
        var account = new Account(uid, UUID.randomUUID(), List.of(new Balance(1, BigDecimal.ZERO, "USD")));
        when(accountRepository.getAccountById(any()))
                .thenReturn(Optional.of(account));

        var curAccount = service.getAccountById(uid.toString());

        assertThat(curAccount)
                .isNotNull()
                .isEqualTo(account);
    }

    @Test
    void getAccountById_uuidException() {
        var incorrect_uuid = "1234";

        assertThatThrownBy(() -> service.getAccountById(incorrect_uuid))
                .isInstanceOf(AccountNotFoundException.class);
    }

    @Test
    void getAccountById_accountNotFound() {
        var uid = UUID.randomUUID();
        when(accountRepository.getAccountById(uid)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> service.getAccountById(uid.toString()))
                .isInstanceOf(AccountNotFoundException.class);
    }

}