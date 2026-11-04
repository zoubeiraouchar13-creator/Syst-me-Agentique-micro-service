package net.aouchar.accountservice;

import net.aouchar.accountservice.domain.AccountType;
import net.aouchar.accountservice.service.EBankService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import javax.security.auth.login.AccountNotFoundException;

@SpringBootApplication
public class AccountServiceApplication {

    public static void main(String[] args) {

        SpringApplication.run(AccountServiceApplication.class, args);
    }
    @Bean
    CommandLineRunner commandLineRunner(EBankService eBankService){
        return args -> {
            eBankService.saveNewAccount(65000, AccountType.CURRENT_ACCOUNT,"MAD", Long.valueOf(1 ));
            eBankService.saveNewAccount(12000, AccountType.SAVING_ACCOUNT,"MAD", Long.valueOf(1 ));
            eBankService.saveNewAccount(3000, AccountType.CURRENT_ACCOUNT,"MAD", Long.valueOf(2 ));

            eBankService.getAllAccounts().forEach(acc-> {
                for (int i = 0; i < 5; i++) {
                    try {
                        eBankService.credit(acc.getId(), 500+Math.random() * 230000);
                    } catch (AccountNotFoundException e) {
                        throw new RuntimeException(e);
                    }
                    try {
                        eBankService.debit(acc.getId(), 500+Math.random()*10000);
                    } catch (AccountNotFoundException e) {
                        throw new RuntimeException(e);
                    }
                }
            });
        };
    }
}
