package com.mindhub.homebanking.controllers;

import com.mindhub.homebanking.dtos.TransactionDTO;
import com.mindhub.homebanking.models.Account;
import com.mindhub.homebanking.models.Transaction;
import com.mindhub.homebanking.models.TransactionType;
import com.mindhub.homebanking.repositories.AccountRepository;
import com.mindhub.homebanking.repositories.ClientRepository;
import com.mindhub.homebanking.repositories.TransactionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api")
public class TransactionController {
    @Autowired
    private TransactionRepository transactionRepository;
    @Autowired
    private AccountRepository accountRepository;
    @Autowired
    private ClientRepository clientRepository;

    @GetMapping("/transactions")
    public List<TransactionDTO> getTransactions() {
        return transactionRepository.findAll().stream().map(transaction -> new TransactionDTO(transaction)).collect(Collectors.toList());
    }

    @GetMapping("/transactions/{id}")
    public TransactionDTO getTransactions(@PathVariable Long id) {
        return new TransactionDTO(transactionRepository.findById(id).orElse(null));
    }

    @Transactional
    @PostMapping("/transactions")
    public ResponseEntity<Object> createTransaction (
            @RequestParam Double amount, @RequestParam String description, @RequestParam String senderAccount, @RequestParam String receiverAccount, Authentication authentication
    ) {
        Account originAccount = accountRepository.findByNumber(senderAccount);
        Account destinyAccount = accountRepository.findByNumber(receiverAccount);

        if (amount.isNaN()) {
            return new ResponseEntity<>("Please enter the amount", HttpStatus.FORBIDDEN);
        }
        if (description.isEmpty()) {
            return new ResponseEntity<>("Please enter the description", HttpStatus.FORBIDDEN);
        }
        if (senderAccount.isEmpty()) {
            return new ResponseEntity<>("Please enter the origin account", HttpStatus.FORBIDDEN);
        }
        if (receiverAccount.isEmpty()) {
            return new ResponseEntity<>("Please enter the destiny account", HttpStatus.FORBIDDEN);
        }
        if (senderAccount.equals(receiverAccount)) {
            return new ResponseEntity<>("The accounts are the same", HttpStatus.FORBIDDEN);
        }
        if (senderAccount == null) {
            return new ResponseEntity<>("The origin account doesn't exist", HttpStatus.FORBIDDEN);
        }
        if (receiverAccount == null) {
            return new ResponseEntity<>("The destination account doesn't exist", HttpStatus.FORBIDDEN);
        }
        if (clientRepository.findByEmail(authentication.getName()).getAccounts().stream().noneMatch(account -> account.getNumber().equals(senderAccount))) {
            return new ResponseEntity<>("The origin account doesn't belong to you", HttpStatus.FORBIDDEN);
        }
        if (amount > originAccount.getBalance()) {
            return new ResponseEntity<>("Insufficient funds", HttpStatus.FORBIDDEN);
        }

        Transaction debit = new Transaction(TransactionType.DEBIT, amount, description, LocalDateTime.now());
        originAccount.addTransaction(debit);
        originAccount.setBalance(originAccount.getBalance() - amount);
        transactionRepository.save(debit);
        accountRepository.save(originAccount);

        Transaction credit = new Transaction(TransactionType.CREDIT, amount, description, LocalDateTime.now());
        destinyAccount.addTransaction(credit);
        destinyAccount.setBalance(destinyAccount.getBalance() + amount);
        transactionRepository.save(credit);
        accountRepository.save(destinyAccount);

        return new ResponseEntity<>("Transaction executed correctly", HttpStatus.CREATED);
    }
}
