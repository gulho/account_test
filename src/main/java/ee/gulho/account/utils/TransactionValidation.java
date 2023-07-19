package ee.gulho.account.utils;

import ee.gulho.account.entity.Balance;
import ee.gulho.account.entity.enums.TransactionDirection;
import ee.gulho.account.exception.TransactionCreateError;
import ee.gulho.account.service.dto.TransactionCreateRequest;
import org.springframework.stereotype.Component;

@Component
public class TransactionValidation {

    public void validate(TransactionCreateRequest request, Balance balance) {
        if (request.direction().equals(TransactionDirection.OUT) && balance.getAmount().compareTo(request.amount()) < 0) {
            throw new TransactionCreateError("Account balance amount is less that in transaction");
        }
    }
}
