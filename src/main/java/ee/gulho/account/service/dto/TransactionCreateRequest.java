package ee.gulho.account.service.dto;

import ee.gulho.account.entity.enums.TransactionDirection;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.util.UUID;

@Data
@AllArgsConstructor
@Builder
public class TransactionCreateRequest {
    @NotNull
    private UUID accountId;
    @Positive
    private BigDecimal amount;
    private String currency;
    private TransactionDirection direction;
    @NotEmpty
    private String description;
}
