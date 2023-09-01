package com.mindhub.homebanking.controllers;

import com.mindhub.homebanking.models.Account;
import com.mindhub.homebanking.repositories.AccountRepository;
import com.mindhub.homebanking.repositories.ClientRepository;
import com.mindhub.homebanking.repositories.TransactionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class TransactionController {
    @Autowired
    private TransactionRepository transactionRepository;
    @Autowired
    private AccountRepository accountRepository;
    @Autowired
    private ClientRepository clientRepository;

    @RequestMapping(path = "transactions", method = RequestMethod.POST)
    public ResponseEntity<Object> createTransaction (
            @RequestParam Double amount, @RequestParam String description, @RequestParam String senderAccount, @RequestParam String receiverAccount, Authentication authentication
    ) {
        if (amount.isNaN()) {
            return new ResponseEntity<>("Please enter the amount", HttpStatus.FORBIDDEN);
        } else if (description.isEmpty()) {
            return new ResponseEntity<>("Please enter the description", HttpStatus.FORBIDDEN);
        } else if (senderAccount.isEmpty()) {
            return new ResponseEntity<>("Please enter the origin account", HttpStatus.FORBIDDEN);
        } else if (receiverAccount.isEmpty()) {
            return new ResponseEntity<>("Please enter the destiny account", HttpStatus.FORBIDDEN);
        } else if (senderAccount.equals(receiverAccount)) {
            return new ResponseEntity<>("The accounts are the same", HttpStatus.FORBIDDEN);
        } else if (senderAccount == null) {
            return new ResponseEntity<>("The origin account doesn't exist", HttpStatus.FORBIDDEN);
        } else if (receiverAccount == null) {
            return new ResponseEntity<>("The destination account doesn't exist", HttpStatus.FORBIDDEN);
        } else if (clientRepository.findByEmail(authentication.getName()).getAccounts().stream().noneMatch(account -> account.getNumber().equals(senderAccount))) {
            return new ResponseEntity<>("The origin account doesn't belong to you", HttpStatus.FORBIDDEN);
        } else if (amount > senderAccount.getBalance()) {
            
        }

    }
    }
    )
}
