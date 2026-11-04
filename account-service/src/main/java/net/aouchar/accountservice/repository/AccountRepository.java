package net.aouchar.accountservice.repository;

import net.aouchar.accountservice.domain.Account;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AccountRepository extends JpaRepository<Account, String> {
    List<Account> findByCustomerId(Long customerId);
}
