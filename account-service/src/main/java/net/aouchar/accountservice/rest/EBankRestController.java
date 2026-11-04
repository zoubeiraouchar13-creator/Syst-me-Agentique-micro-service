package net.aouchar.accountservice.rest;

import net.aouchar.accountservice.domain.Account;
import net.aouchar.accountservice.domain.AccountTransaction;
import net.aouchar.accountservice.domain.AccountType;
import net.aouchar.accountservice.exceptions.NegativeAmountException;
import net.aouchar.accountservice.service.EBankService;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import javax.security.auth.login.AccountNotFoundException;
import java.util.List;
import java.util.Map;

@RestController
public class EBankRestController {
    private EBankService eBankService;

    public EBankRestController(EBankService eBankService) {
        this.eBankService = eBankService;
    }
    @GetMapping("/accounts/{accountId}")
    public Account getAccountById(@PathVariable String accountId) throws AccountNotFoundException {
        return eBankService.getAccountById(accountId);
    }

    @GetMapping("/accounts")
    public List<Account> getAllAccounts(@RequestParam(required = false) Long customerId) {
        return customerId == null
                ? eBankService.getAllAccounts()
                : eBankService.getAccountsByCustomer(customerId);
    }

    @PostMapping("/accounts/save")
    public Account saveNewAccount(double initialBalance, AccountType type, String currency, Long customerId) throws NegativeAmountException {
        return eBankService.saveNewAccount(initialBalance, type, currency, customerId);
    }

    @PostMapping("/accounts/credit")
    public AccountTransaction credit(String accountId, double amount) throws AccountNotFoundException {
        return eBankService.credit(accountId, amount);
    }

    @PostMapping("/accounts/debit")
    public AccountTransaction debit(String accountId, double amount) throws AccountNotFoundException {
        return eBankService.debit(accountId, amount);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity exceptionHandler(Exception e){
        return ResponseEntity.internalServerError().body(Map.of("errorMessage",e.getMessage()));
    }
}
