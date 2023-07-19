package ee.gulho.account.service.dto;

import ee.gulho.account.utils.AllowedCurrencies;
import jakarta.validation.constraints.NotNull;

import java.util.List;
import java.util.UUID;


public record AccountCreateRequest (

    @NotNull
    UUID customerId,
    String country,

    @AllowedCurrencies
    List<String> currencies

){}
