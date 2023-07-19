package ee.gulho.account.controller;

import ee.gulho.account.entity.Account;
import ee.gulho.account.service.AccountService;
import ee.gulho.account.service.dto.AccountCreateRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("account/")
@RequiredArgsConstructor
public class AccountController implements BaseController {

    private final AccountService service;

    @PostMapping
    ResponseEntity<Account> createAccount(@Valid @RequestBody AccountCreateRequest request) {
        return ResponseEntity
                .ok(service.createAccount(request));
    }

    @GetMapping("{id}")
    ResponseEntity<Account> getAccount(@PathVariable String id) {
        return ResponseEntity
                .ok(service.getAccountById((id)));
    }
}
