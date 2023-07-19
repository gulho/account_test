package ee.gulho.account.service;

import ee.gulho.account.entity.Account;
import ee.gulho.account.exception.AccountNotFoundException;
import ee.gulho.account.mapper.AccountRepository;
import ee.gulho.account.mapper.BalanceRepository;
import ee.gulho.account.service.dto.AccountCreateRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AccountService {

    private final AccountRepository accountRepository;
    private final BalanceRepository balanceRepository;

    private static final BigDecimal balanceCreatedAmount = BigDecimal.ZERO;

    @Transactional
    public Account createAccount(AccountCreateRequest request) {
        var uid = UUID.randomUUID();
        accountRepository.insertAccount(uid, request.getCustomerId());
        request.getCurrencies()
                .forEach(currency -> createNewBalance(currency, uid));

        var createdAccount = getAccountById(uid.toString());

        return createdAccount;
    }

    public Account getAccountById(String id) {
        try {
            var uid = UUID.fromString(id);
            return getAccountById(uid).orElseThrow();
        } catch (Exception ex) {
            throw new AccountNotFoundException("String UUID is incorrect");
        }
    }

    public Optional<Account> getAccountById(UUID id) {
        return accountRepository.getAccountById(id);
    }

    private void createNewBalance(String currency, UUID accountId) {
        balanceRepository.insertBalance(balanceCreatedAmount, currency, accountId);
    }
}
