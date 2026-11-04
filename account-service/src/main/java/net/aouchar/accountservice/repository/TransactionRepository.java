package net.aouchar.accountservice.repository;

import net.aouchar.accountservice.domain.Account;
import net.aouchar.accountservice.domain.AccountTransaction;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TransactionRepository extends JpaRepository<AccountTransaction, Long> {
    List<AccountTransaction> findByAccountId(String accountId);
}
