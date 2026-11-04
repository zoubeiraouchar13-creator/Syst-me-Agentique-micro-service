package net.aouchar.accountservice.mcp;
import net.aouchar.accountservice.domain.Account;
import net.aouchar.accountservice.domain.AccountTransaction;
import net.aouchar.accountservice.domain.AccountType;
import net.aouchar.accountservice.exceptions.NegativeAmountException;
import net.aouchar.accountservice.rest.EBankRestController;
import net.aouchar.accountservice.service.EBankService;
import org.springframework.ai.mcp.annotation.McpArg;
import org.springframework.ai.mcp.annotation.McpTool;
import org.springframework.ai.mcp.annotation.McpToolParam;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import javax.security.auth.login.AccountNotFoundException;
import java.util.List;

@Component
public class EBankMCPController {
    private EBankService eBankService;

    public EBankMCPController(EBankService eBankService){
        this.eBankService = eBankService;
    }

    @McpTool(description = "Get Account By Id")
    public Account getAccountById(@McpToolParam(description ="The account id") String accountId) throws AccountNotFoundException {
        return eBankService.getAccountById(accountId);
    }

    @McpTool(description = "Get all accounts")
    public List<Account> getAllAccounts() {
        return eBankService.getAllAccounts();
    }

    public List<Account> getAccountsByCustomer(Long customerId) {
        return eBankService.getAccountsByCustomer(customerId);
    }

    @McpTool(description = "Save a new account")
    public Account saveNewAccount(
            @McpArg(description = "The initial balance") double initialBalance,
            @McpArg(description = "Account type") AccountType type,
            @McpArg(description = "The Account currency") String currency,
            @McpArg(description = "The custumer Id") Long customerId) throws NegativeAmountException {
        return eBankService.saveNewAccount(initialBalance, type, currency, customerId);
    }

    @McpTool(description = "Credit account")
    public AccountTransaction credit(
            @McpArg(description = "The account Id")String accountId,
            @McpArg(description = "The credited amount") double amount) throws AccountNotFoundException {
        return eBankService.credit(accountId, amount);
    }

    @McpTool(description = "Debit account")
    public AccountTransaction debit(
            @McpArg(description = "The account Id") String accountId,
            @McpArg(description = "The debited amount") double amount) throws AccountNotFoundException {
        return eBankService.debit(accountId, amount);
    }

}
