package ee.gulho.account.utils;

import ee.gulho.account.entity.Balance;
import ee.gulho.account.entity.enums.TransactionDirection;
import ee.gulho.account.exception.TransactionCreateError;
import ee.gulho.account.service.dto.TransactionCreateRequest;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThatNoException;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class TransactionValidationTest {

    private static final UUID ACCOUNT_ID = UUID.randomUUID();
    private static final BigDecimal AMOUNT = BigDecimal.TEN;
    private static final String CURRENCY = "USD";
    private static final TransactionDirection DIRECTION = TransactionDirection.IN;
    private static final String DESCRIPTION = "Some Mock description";

    private final TransactionValidation validation = new TransactionValidation();
    @Test
    void validate_success() {
        var request = TransactionCreateRequest.builder()
                .accountId(ACCOUNT_ID)
                .amount(AMOUNT)
                .currency(CURRENCY)
                .direction(DIRECTION)
                .description(DESCRIPTION)
                .build();
        var balance = Balance.builder()
                .id(10)
                .amount(BigDecimal.TEN)
                .currency(CURRENCY)
                .build();

        assertThatNoException().isThrownBy(() ->validation.validate(request, balance));
    }

    @Test
    void validate_throwException() {
        var request = TransactionCreateRequest.builder()
                .accountId(ACCOUNT_ID)
                .amount(AMOUNT)
                .currency(CURRENCY)
                .direction(TransactionDirection.OUT)
                .description(DESCRIPTION)
                .build();
        var balance = Balance.builder()
                .id(10)
                .amount(BigDecimal.ZERO)
                .currency(CURRENCY)
                .build();

        assertThatThrownBy(() ->validation.validate(request, balance))
                .isInstanceOf(TransactionCreateError.class);
    }
}