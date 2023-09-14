package com.mindhub.homebanking.controllers;

import com.mindhub.homebanking.dtos.TransactionDTO;
import com.mindhub.homebanking.models.Account;
import com.mindhub.homebanking.models.Transaction;
import com.mindhub.homebanking.models.TransactionType;
import com.mindhub.homebanking.repositories.AccountRepository;
import com.mindhub.homebanking.repositories.ClientRepository;
import com.mindhub.homebanking.repositories.TransactionRepository;
import com.mindhub.homebanking.services.AccountService;
import com.mindhub.homebanking.services.ClientService;
import com.mindhub.homebanking.services.TransactionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api")
public class TransactionController {
    @Autowired
    private TransactionService transactionService;
    @Autowired
    private ClientService clientService;
    @Autowired
    private AccountService accountService;

    @GetMapping("/transactions")
    public List<TransactionDTO> getTransactions() {
        return transactionService.getTransactions();
    }

    @GetMapping("/transactions/{id}")
    public TransactionDTO getTransactions(@PathVariable Long id) {
        return transactionService.getTransactions(id);
    }

    @Transactional
    @PostMapping("/transactions")
    public ResponseEntity<Object> createTransaction (
            @RequestParam Double amount, @RequestParam String description, @RequestParam String fromAccountNumber, @RequestParam String toAccountNumber, Authentication authentication
    ) {
        Account originAccount = accountService.findByNumber(fromAccountNumber);
        Account destinyAccount = accountService.findByNumber(toAccountNumber);

        if (amount.isNaN()) {
            return new ResponseEntity<>("Please enter the amount", HttpStatus.FORBIDDEN);
        }
        if (description.isEmpty()) {
            return new ResponseEntity<>("Please enter the description", HttpStatus.FORBIDDEN);
        }
        if (fromAccountNumber.isEmpty()) {
            return new ResponseEntity<>("Please enter the origin account", HttpStatus.FORBIDDEN);
        }
        if (toAccountNumber.isEmpty()) {
            return new ResponseEntity<>("Please enter the destiny account", HttpStatus.FORBIDDEN);
        }
        if (fromAccountNumber.equals(toAccountNumber)) {
            return new ResponseEntity<>("The accounts are the same", HttpStatus.FORBIDDEN);
        }
        if (fromAccountNumber == null) {
            return new ResponseEntity<>("The origin account doesn't exist", HttpStatus.FORBIDDEN);
        }
        if (toAccountNumber == null) {
            return new ResponseEntity<>("The destination account doesn't exist", HttpStatus.FORBIDDEN);
        }
        if (clientService.findByEmail(authentication.getName()).getAccounts().stream().noneMatch(account -> account.getNumber().equals(fromAccountNumber))) {
            return new ResponseEntity<>("The origin account doesn't belong to you", HttpStatus.FORBIDDEN);
        }
        if (amount > originAccount.getBalance()) {
            return new ResponseEntity<>("Insufficient funds", HttpStatus.FORBIDDEN);
        }

        Transaction debit = new Transaction(TransactionType.DEBIT, amount, description, LocalDateTime.now());
        originAccount.addTransaction(debit);
        originAccount.setBalance(originAccount.getBalance() - amount);
        transactionService.saveTransaction(debit);
        accountService.saveAccount(originAccount);

        Transaction credit = new Transaction(TransactionType.CREDIT, amount, description, LocalDateTime.now());
        destinyAccount.addTransaction(credit);
        destinyAccount.setBalance(destinyAccount.getBalance() + amount);
        transactionService.saveTransaction(credit);
        accountService.saveAccount(destinyAccount);

        return new ResponseEntity<>("Transaction executed correctly", HttpStatus.CREATED);
    }
}
