package ee.gulho.account.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

@Data
@AllArgsConstructor
@Builder
public class Balance implements Serializable {
    private Integer id;
    private BigDecimal amount;
    private String currency;
}
