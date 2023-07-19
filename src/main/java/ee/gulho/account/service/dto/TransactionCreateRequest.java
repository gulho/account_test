package ee.gulho.account.service.dto;

import ee.gulho.account.entity.enums.TransactionDirection;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Builder;

import java.math.BigDecimal;
import java.util.UUID;

@Builder
public record TransactionCreateRequest(
        @NotNull
        UUID accountId,
        @Positive
        BigDecimal amount,
        @NotEmpty
        String currency,
        TransactionDirection direction,
        @NotEmpty
        String description
) {

}
