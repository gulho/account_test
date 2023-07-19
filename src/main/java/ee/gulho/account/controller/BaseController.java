package ee.gulho.account.controller;

import ee.gulho.account.exception.AccountNotFoundException;
import ee.gulho.account.exception.TransactionCreateError;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.util.HashMap;
import java.util.Map;

public interface BaseController {
    @ResponseStatus(value = HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    default Map<String, String> handleValidationExceptions(
            MethodArgumentNotValidException ex) {
        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getAllErrors().forEach(error -> {
            String fieldName = ((FieldError) error).getField();
            String errorMessage = error.getDefaultMessage();
            errors.put(fieldName, errorMessage);
        });
        return errors;
    }

    @ResponseStatus(value = HttpStatus.BAD_REQUEST)
    @ExceptionHandler(AccountNotFoundException.class)
    default ResponseEntity<String> handleAccountNotFound(AccountNotFoundException ex) {
        return ResponseEntity
                .badRequest()
                .body(ex.getMessage());
    }

    @ResponseStatus(value = HttpStatus.BAD_REQUEST)
    @ExceptionHandler(TransactionCreateError.class)
    default ResponseEntity<String> handleAccountNotFound(TransactionCreateError ex) {
        return ResponseEntity
                .badRequest()
                .body(ex.getMessage());
    }
}
