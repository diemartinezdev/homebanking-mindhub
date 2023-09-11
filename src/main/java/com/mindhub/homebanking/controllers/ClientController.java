package com.mindhub.homebanking.controllers;

import com.mindhub.homebanking.dtos.ClientDTO;
import com.mindhub.homebanking.models.Account;
import com.mindhub.homebanking.models.Client;
import com.mindhub.homebanking.services.AccountService;
import com.mindhub.homebanking.services.ClientService;
import com.mindhub.homebanking.utilities.Utility;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api")
public class ClientController {
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private ClientService clientService;
    @Autowired
    private AccountService accountService;


    @GetMapping("/clients")
    private List<ClientDTO> getClients() {
        return clientService.getClients();
    }

    @GetMapping("/clients/{id}")
    private ClientDTO getClients(@PathVariable Long id) {
        return clientService.getClients(id);
    }

    @PostMapping("/clients")
    public ResponseEntity<Object> register(
            @RequestParam String firstName, @RequestParam String lastName, @RequestParam String email, @RequestParam String password
    ) {
        if (firstName.isEmpty()) {
            return new ResponseEntity<>("Please complete the first name", HttpStatus.FORBIDDEN);
        }
        if (lastName.isEmpty()) {
            return new ResponseEntity<>("Please complete the last name", HttpStatus.FORBIDDEN);
        }
        if (email.isEmpty()) {
            return new ResponseEntity<>("Please complete the email", HttpStatus.FORBIDDEN);
        }
        if (password.isEmpty()) {
            return new ResponseEntity<>("Please complete the password", HttpStatus.FORBIDDEN);
        }
        if (clientService.findByEmail(email) !=  null) {
            return new ResponseEntity<>("Email already in use", HttpStatus.FORBIDDEN);
        }

            Client newClient = new Client(firstName, lastName, email, passwordEncoder.encode(password));
            clientService.saveClient(newClient);
            Account newAccount = new Account("VIN" + Utility.getRandomNumber(00000000, 99999999), LocalDate.now(), 0.0, newClient);
            accountService.saveAccount(newAccount);
            return new ResponseEntity<>("Account created :D", HttpStatus.CREATED);
    }
       /*
       if (firstName.isEmpty() || lastName.isEmpty() || email.isEmpty() || password.isEmpty()) {
            return new ResponseEntity<>("Missing data", HttpStatus.FORBIDDEN);
        }
        if (clientRepository.findByEmail(email) !=  null) {
            return new ResponseEntity<>("Name already in use", HttpStatus.FORBIDDEN);
        }
        clientRepository.save(new Client(firstName, lastName, email, passwordEncoder.encode(password)));
        return new ResponseEntity<>(HttpStatus.CREATED);
        */

    @GetMapping("/clients/current")
    public ClientDTO getCurrent (Authentication authentication) {
        return clientService.getCurrent(authentication);
    }
}

