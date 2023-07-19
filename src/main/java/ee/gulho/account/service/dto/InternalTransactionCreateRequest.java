package ee.gulho.account.service.dto;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Builder;

import java.math.BigDecimal;
import java.util.UUID;

@Builder
public record InternalTransactionCreateRequest(
        @NotNull UUID accountOut,
        @NotNull UUID accountIn,
        @NotEmpty String currency,
        @Positive BigDecimal amount,
        @NotEmpty String description
) {
}
