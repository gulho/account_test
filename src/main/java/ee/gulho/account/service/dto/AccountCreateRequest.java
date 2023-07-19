package ee.gulho.account.service.dto;

import ee.gulho.account.utils.AllowedCurrencies;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AccountCreateRequest {

    @NotNull
    private UUID customerId;
    private String country;

    @AllowedCurrencies
    private List<String> currencies;

}
