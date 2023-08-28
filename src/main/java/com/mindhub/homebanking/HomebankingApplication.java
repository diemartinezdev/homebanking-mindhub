package com.mindhub.homebanking;

import com.mindhub.homebanking.models.*;
import com.mindhub.homebanking.repositories.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Arrays;


@SpringBootApplication
public class HomebankingApplication {

	@Autowired
	private PasswordEncoder passwordEncoder;
	public static void main(String[] args) {
		SpringApplication.run(HomebankingApplication.class, args);
	}

	@Bean
	public CommandLineRunner initData(ClientRepository clientRepository, AccountRepository accountRepository, TransactionRepository transactionRepository, LoanRepository loanRepository, ClientLoanRepository clientLoanRepository, CardRepository cardRepository) {
		return (args) -> {

			Client client1 = new Client("Melba", "Morel", "melba@mindhub.com", passwordEncoder.encode("melba"));
			clientRepository.save(client1);

			Account account1 = new Account("VIN0001", LocalDate.now(), 5000, client1);
			Account account2 = new Account("VIN0002", LocalDate.now().plusDays(1), 7500, client1);

			client1.addAccount(account1);
			client1.addAccount(account2);

			accountRepository.save(account1);
			accountRepository.save(account2);

			Client client2 = new Client("Diego", "Martinez", "martinez.diego90@gmail.com",passwordEncoder.encode("Password"));
			Account account3 = new Account("VIN0003", LocalDate.now(), 9000, client2);
			client2.addAccount(account3);
			clientRepository.save(client2);
			accountRepository.save(account3);

			Transaction transaction1 = new Transaction(TransactionType.DEBIT, 250.0,"debt", LocalDateTime.now());
			account1.addTransaction(transaction1);
			Transaction transaction2 = new Transaction(TransactionType.DEBIT, 300.0, "loan", LocalDateTime.now());
			account3.addTransaction(transaction2);
			Transaction transaction3 = new Transaction(TransactionType.CREDIT, 500.0, "tv", LocalDateTime.now());
			account2.addTransaction(transaction3);
			transactionRepository.save(transaction1);
			transactionRepository.save(transaction2);
			transactionRepository.save(transaction3);

			Loan loan1 = new Loan("Mortgage", 500000.0, Arrays.asList(12, 24, 36, 48, 60));
			Loan loan2 = new Loan("Personal", 100000.0, Arrays.asList(6,12,24));
			Loan loan3 = new Loan("Automotive", 300000.0, Arrays.asList(6,12,24,36));
			loanRepository.save(loan1);
			loanRepository.save(loan2);
			loanRepository.save(loan3);

			ClientLoan clientLoan1 = new ClientLoan(400000.0, 60);
			ClientLoan clientLoan2 = new ClientLoan(50000.0, 12);
			client1.addClientLoan(clientLoan1);
			loan1.addClientLoan(clientLoan1);
			client1.addClientLoan(clientLoan2);
			loan2.addClientLoan(clientLoan2);
			clientLoanRepository.save(clientLoan1);
			clientLoanRepository.save(clientLoan2);
			ClientLoan clientLoan3 = new ClientLoan(100000.0, 24);
			ClientLoan clientLoan4 = new ClientLoan(200000.0, 36);
			client2.addClientLoan(clientLoan3);
			loan2.addClientLoan(clientLoan3);
			client2.addClientLoan(clientLoan4);
			loan3.addClientLoan(clientLoan4);
			clientLoanRepository.save(clientLoan3);
			clientLoanRepository.save(clientLoan4);

			Card card1 = new Card(client1.getFirstName() + " " + client1.getLastName(),CardType.DEBIT,CardColor.GOLD,"1234-1234-1234-1234",123,LocalDate.now(),LocalDate.now().plusYears(5));
			Card card2 = new Card(client1.getFirstName() + " " + client1.getLastName(),CardType.CREDIT,CardColor.TITANIUM,"1234-1234-1234-4321",432,LocalDate.now(),LocalDate.now().plusYears(5));
			Card card3 = new Card(client2.getFirstName() + " " + client2.getLastName(),CardType.CREDIT,CardColor.SILVER,"3456-3456-3456-3456",345,LocalDate.now(),LocalDate.now().plusMonths(11));
			client1.addCard(card1);
			client1.addCard(card2);
			client2.addCard(card3);
			cardRepository.save(card1);
			cardRepository.save(card2);
			cardRepository.save(card3);

			// 6
			Client admin = new Client("admin","admin","admin@diebanking.com", passwordEncoder.encode("admin"));
			clientRepository.save(admin);
		};
	}
}
