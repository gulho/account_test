package ee.gulho.account.utils;


import ee.gulho.account.configuration.AppConfiguration;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import lombok.RequiredArgsConstructor;

import java.util.List;

@RequiredArgsConstructor
public class AllowedCurrenciesValidator implements ConstraintValidator<AllowedCurrencies, List<String>> {

    private final AppConfiguration configuration;

    @Override
    public void initialize(AllowedCurrencies constraintAnnotation) {
        ConstraintValidator.super.initialize(constraintAnnotation);
    }

    @Override
    public boolean isValid(List<String> currencies, ConstraintValidatorContext context) {
        return isNoForbiddenCurrencies(currencies);
    }

    private boolean isNoForbiddenCurrencies(List<String> currencies) {
        return currencies.stream().noneMatch(this::isCorriesNotInTheList);
    }

    private boolean isCorriesNotInTheList(String c) {
        return !configuration.getCurrencies().contains(c);
    }
}
