package com.mindhub.homebanking;

import com.mindhub.homebanking.models.*;
import com.mindhub.homebanking.repositories.*;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import static com.mindhub.homebanking.models.TransactionType.CREDIT;
import static com.mindhub.homebanking.models.TransactionType.DEBIT;


@SpringBootApplication
public class HomebankingApplication {

	public static void main(String[] args) {
		SpringApplication.run(HomebankingApplication.class, args);
	}

	@Bean
	public CommandLineRunner initData(ClientRepository clientRepository, AccountRepository accountRepository, TransactionRepository transactionRepository, LoanRepository loanRepository) {
		return (args) -> {

			Client client1 = new Client("Melba", "Morel", "melba@mindhub.com");
			clientRepository.save(client1);

			Account account1 = new Account("VIN0001", LocalDate.now(), 5000);
			Account account2 = new Account("VIN0002", LocalDate.now().plusDays(1), 7500);

			client1.addAccount(account1);
			client1.addAccount(account2);

			accountRepository.save(account1);
			accountRepository.save(account2);

			Client client2 = new Client("Diego", "Martinez", "martinez.diego90@gmail.com");
			Account account3 = new Account("VIN0003", LocalDate.now(), 9000);
			client2.addAccount(account3);
			clientRepository.save(client2);
			accountRepository.save(account3);

			Transaction transaction1 = new Transaction(DEBIT, 250.0,"debt", LocalDateTime.now());
			account1.addTransaction(transaction1);
			Transaction transaction2 = new Transaction(DEBIT, 300.0, "loan", LocalDateTime.now());
			account3.addTransaction(transaction2);
			Transaction transaction3 = new Transaction(CREDIT, 500.0, "tv", LocalDateTime.now());
			account2.addTransaction(transaction3);
			transactionRepository.save(transaction1);
			transactionRepository.save(transaction2);
			transactionRepository.save(transaction3);

			Loan loan1 = new Loan("Mortgage", 500000.0, List.of(12, 24, 36, 48, 60));
			Loan loan2 = new Loan("Personal", 100000.0, List.of(6,12,24));
			Loan loan3 = new Loan("Automotive", 300000.0, List.of(6,12,24,36));
			loanRepository.save(loan1);
			loanRepository.save(loan2);
			loanRepository.save(loan3);
		};
	}
}
