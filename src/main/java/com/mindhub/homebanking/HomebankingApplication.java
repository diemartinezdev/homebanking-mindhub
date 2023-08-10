package com.mindhub.homebanking;

import com.mindhub.homebanking.models.*;
import com.mindhub.homebanking.repositories.*;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.time.LocalDate;


@SpringBootApplication
public class HomebankingApplication {

	public static void main(String[] args) {
		SpringApplication.run(HomebankingApplication.class, args);
	}

	@Bean
	public CommandLineRunner initData(ClientRepository clientRepository, AccountRepository accountRepository) {
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

		};
	}
}
