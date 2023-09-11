package com.mindhub.homebanking.controllers;

import com.mindhub.homebanking.dtos.AccountDTO;
import com.mindhub.homebanking.models.Account;
import com.mindhub.homebanking.models.Client;
import com.mindhub.homebanking.services.AccountService;
import com.mindhub.homebanking.services.ClientService;
import com.mindhub.homebanking.utilities.Utility;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api")
public class AccountController {
    @Autowired
    private AccountService accountService;
    @Autowired
    private ClientService clientService;

    @GetMapping("/accounts")
    public List<AccountDTO> getAccounts() {
        return accountService.getAccounts();
    }

    @GetMapping("/accounts/{id}")
    public AccountDTO getAccounts(@PathVariable Long id) {
        return accountService.getAccounts(id);
    }

    @GetMapping("/clients/current/accounts")
    @ResponseBody
    public List<AccountDTO> getCurrentClientAccounts(Authentication authentication) {
        return accountService.getCurrentClientAccounts(authentication);
    }

    @PostMapping("/clients/current/accounts")
    public ResponseEntity<Object> create(
            Authentication authentication
    ) {
        Client newClient = clientService.findByEmail(authentication.getName());

        if (newClient.getAccounts().size() > 2) {
            return new ResponseEntity<>("Already have 3 accounts", HttpStatus.FORBIDDEN);
        }

        Account account = new Account("VIN" + Utility.getRandomNumber(00000000, 99999999), LocalDate.now(), 0.0, newClient);
        accountService.saveAccount(account);

        return new ResponseEntity<>("Account created :D", HttpStatus.CREATED);
        }
    }