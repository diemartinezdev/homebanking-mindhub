package com.mindhub.homebanking.controllers;

import com.mindhub.homebanking.dtos.LoanApplicationDTO;
import com.mindhub.homebanking.dtos.LoanDTO;
import com.mindhub.homebanking.models.*;
import com.mindhub.homebanking.repositories.*;
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
public class LoanController {
    @Autowired
    private LoanRepository loanRepository;
    @Autowired
    private ClientRepository clientRepository;
    @Autowired
    private AccountRepository accountRepository;
    @Autowired
    private ClientLoanRepository clientLoanRepository;
    @Autowired
    private TransactionRepository transactionRepository;

    @GetMapping("/loans")
    public List<LoanDTO> getLoans() {
        return loanRepository.findAll().stream().map(loan -> new LoanDTO(loan)).collect(Collectors.toList());
    }

    @Transactional
    @PostMapping("/loans")
    public ResponseEntity<Object> createLoan(
            @RequestBody LoanApplicationDTO loanApplicationDTO, Authentication authentication
            ) {
        Client client = clientRepository.findByEmail(authentication.getName());
        Loan loan = loanRepository.findByName(loanApplicationDTO.getLoanId());
        Account destinyAccount = accountRepository.findByNumber(loanApplicationDTO.getDestinationAccount());

        if (loanApplicationDTO.getLoanId() == null || loanApplicationDTO.getAmount() == null || loanApplicationDTO.getPayments() == null || loanApplicationDTO.getDestinationAccount() == null) {
            return new ResponseEntity<>("Missing data", HttpStatus.FORBIDDEN);
        }
        if (loanApplicationDTO.getAmount() <= 0) {
            return new ResponseEntity<>("Incorrect amount", HttpStatus.FORBIDDEN);
        }
        if (loanApplicationDTO.getPayments() <= 0) {
            return new ResponseEntity<>("Incorrect number of payments", HttpStatus.FORBIDDEN);
        }
        if (loan == null) {
            return new ResponseEntity<>("Loan doesn't exist", HttpStatus.FORBIDDEN);
        }
        if (loanApplicationDTO.getAmount() > loan.getMaxAmount()) {
            return new ResponseEntity<>("Amount requested exceeds the maximum of the loan", HttpStatus.FORBIDDEN);
        }
        if (!loan.getPayments().contains(loanApplicationDTO.getPayments())) {
            return new ResponseEntity<>("Payments requested exceeds the maximum of the loan", HttpStatus.FORBIDDEN);
        }
        if (destinyAccount == null) {
            return new ResponseEntity<>("Destiny account doesn't exist", HttpStatus.FORBIDDEN);
        }
        if (!client.getAccounts().contains(destinyAccount)) {
            return new ResponseEntity<>("The account doesn't correspond to the client", HttpStatus.FORBIDDEN);
        }

        Double loanRevenue = (loanApplicationDTO.getAmount() * 0.20) + (loanApplicationDTO.getAmount());

        ClientLoan newLoan = new ClientLoan(loanRevenue, loanApplicationDTO.getPayments());
        Transaction newTransaction = new Transaction(TransactionType.CREDIT, loanApplicationDTO.getAmount(), loan.getName() + " - Loan approved", LocalDateTime.now());

        destinyAccount.setBalance(destinyAccount.getBalance() + loanApplicationDTO.getAmount());
        destinyAccount.addTransaction(newTransaction);

        client.addClientLoan(newLoan);
        loan.addClientLoan(newLoan);

        transactionRepository.save(newTransaction);
        clientLoanRepository.save(newLoan);
        accountRepository.save(destinyAccount);
        loanRepository.save(loan);
        clientRepository.save(client);

        return new ResponseEntity<>("Loan applied correctly", HttpStatus.CREATED);
    }
}
