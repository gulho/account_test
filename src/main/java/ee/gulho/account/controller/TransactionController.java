package ee.gulho.account.controller;

import ee.gulho.account.entity.Transaction;
import ee.gulho.account.service.TransactionService;
import ee.gulho.account.service.dto.TransactionCreateRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("transaction/")
@RequiredArgsConstructor
public class TransactionController implements BaseController {

    private final TransactionService service;

    @PostMapping
    ResponseEntity<Transaction> createTransaction(@Valid @RequestBody TransactionCreateRequest transactionCreate) {
        return ResponseEntity.ok(service.createTransaction(transactionCreate));
    }

    @GetMapping("{transactionId}")
    ResponseEntity<Transaction> getTransaction(@PathVariable String transactionId) {

        return ResponseEntity.ok(service.getTransaction(transactionId));
    }
}
