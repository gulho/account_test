package ee.gulho.account.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Transaction implements Serializable {
    private UUID accountId;
    private UUID transactionId;
    private BigDecimal amount;
    private String currency;
    private String direction;
    private String description;
    private BigDecimal balanceAmount;
}
