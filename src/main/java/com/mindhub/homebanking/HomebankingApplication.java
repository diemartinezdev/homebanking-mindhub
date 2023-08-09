package com.mindhub.homebanking;

import com.mindhub.homebanking.models.*;
import com.mindhub.homebanking.repositories.*;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.time.LocalDateTime;


@SpringBootApplication
public class HomebankingApplication {

	public static void main(String[] args) {
		SpringApplication.run(HomebankingApplication.class, args);
	}

	@Bean
	public CommandLineRunner initData(ClientRepository clientRepository, AccountRepository accountRepository) {
		return (args) -> {

			Client client1 = new Client("Melba", "Morel", "melba@mindhub.com");
			Client client2 = new Client("Diego", "Martinez", "diemartinez.dev@gmail.com");

			clientRepository.save(client1);
			clientRepository.save(client2);

			LocalDateTime creationDate = LocalDateTime.now();
			LocalDateTime tomorrow = creationDate.plusDays(1);

			Account account1 = new Account("VIN0001", creationDate, 5000);
			Account account2 = new Account("VIN0002", tomorrow, 7500);

			accountRepository.save(account1);
			accountRepository.save(account2);

		};
	}
}
