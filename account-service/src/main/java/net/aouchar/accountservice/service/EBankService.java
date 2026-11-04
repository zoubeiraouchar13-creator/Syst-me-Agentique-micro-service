package net.aouchar.accountservice.service;

import net.aouchar.accountservice.domain.*;
import net.aouchar.accountservice.exceptions.BalanceNotSufficientException;
import net.aouchar.accountservice.exceptions.NegativeAmountException;
import net.aouchar.accountservice.repository.AccountRepository;
import net.aouchar.accountservice.repository.TransactionRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.security.auth.login.AccountNotFoundException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
//CQRS (Command Query Responsibility Segregation)
@Service
public class EBankService {
    private AccountRepository accountRepository;
    private TransactionRepository transactionRepository;

    public EBankService(AccountRepository accountRepository, TransactionRepository transactionRepository){
        this.accountRepository = accountRepository;
        this.transactionRepository = transactionRepository;
    }

    public Account getAccountById(String accountId) throws AccountNotFoundException {
        Account account = accountRepository.findById(accountId).orElseThrow(
                () -> new AccountNotFoundException("Account not found"));
        return account;
    }

    public List<Account> getAllAccounts() {
        return accountRepository.findAll();
    }
    public List<Account> getAccountsByCustomer(Long customerId) {
        return accountRepository.findByCustomerId(customerId);
    }
    public Account saveNewAccount(double initialBalance, AccountType type,
                                  String currency, Long customerId) throws NegativeAmountException {
        if(initialBalance<0) throw
            new NegativeAmountException("The amount should be positive");
        Account account = Account.builder()
                .id(UUID.randomUUID().toString())
                .currency(currency)
                .type(type)
                .status(AccountStatus.CREATED)
                .createdAt(LocalDateTime.now())
                .customerId(customerId)
                .balance(initialBalance)
                .build();
        Account savedAccount = accountRepository.save(account);
        AccountTransaction transaction = AccountTransaction.builder()
                .amount(initialBalance)
                .type(TransactionType.CREDIT)
                .dateTime(LocalDateTime.now())
                .account(savedAccount)
                .build();
        transactionRepository.save(transaction);
        return savedAccount;
    }

    @Transactional
    public AccountTransaction credit(String accountId, double amount) throws AccountNotFoundException {
        if(amount<=0) throw new NegativeAmountException("Amount should be positive");
        Account account = getAccountById(accountId);
        AccountTransaction accountTransaction =
                AccountTransaction.builder()
                        .amount(amount)
                        .dateTime(LocalDateTime.now())
                        .type(TransactionType.CREDIT)
                        .account(account)
                        .build();
        account.setBalance(account.getBalance()+amount);
        Account savedAccount = accountRepository.save(account);
        return transactionRepository.save(accountTransaction);
    }

    @Transactional
    public AccountTransaction debit(String accountId, double amount) throws AccountNotFoundException {
        if(amount<=0) throw new NegativeAmountException("Amount should be positive");
        Account account = getAccountById(accountId);
        if(account.getBalance()<amount) throw new BalanceNotSufficientException("Balance Not Sufficient Exception");
        AccountTransaction accountTransaction =
                AccountTransaction.builder()
                        .amount(amount)
                        .dateTime(LocalDateTime.now())
                        .type(TransactionType.DEBIT)
                        .account(account)
                        .build();

        AccountTransaction transaction = transactionRepository.save(accountTransaction);
        account.setBalance(account.getBalance()-amount);
        Account savedAccount = accountRepository.save(account);

        return transaction;
    }
}
