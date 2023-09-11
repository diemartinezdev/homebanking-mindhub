package com.mindhub.homebanking.controllers;

import com.mindhub.homebanking.dtos.LoanApplicationDTO;
import com.mindhub.homebanking.dtos.LoanDTO;
import com.mindhub.homebanking.models.*;
import com.mindhub.homebanking.services.*;
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
public class LoanController {
    @Autowired
    private LoanService loanService;
    @Autowired
    private ClientService clientService;
    @Autowired
    private AccountService accountService;
    @Autowired
    private ClientLoanService clientLoanService;
    @Autowired
    private TransactionService transactionService;

    @GetMapping("/loans")
    public List<LoanDTO> getLoans() {
        return loanService.getAllLoans();
    }

    @Transactional
    @PostMapping("/loans")
    public ResponseEntity<Object> createLoan(
            @RequestBody LoanApplicationDTO loanApplicationDTO, Authentication authentication
    ) {

        Client client = clientService.findByEmail(authentication.getName());
        Loan newLoan = loanService.getLoanById(loanApplicationDTO.getLoanId());
        Account destinyAccount = accountService.findByNumber(loanApplicationDTO.getToAccountNumber());

        if (loanApplicationDTO.getAmount() <= 0 || loanApplicationDTO.getPayments() <= 0) {
            return new ResponseEntity<>("Missing parameters", HttpStatus.FORBIDDEN);
        }
        if (loanApplicationDTO.getAmount() > newLoan.getMaxAmount()) {
            return new ResponseEntity<>("Amount requested exceeds the maximum of the loan", HttpStatus.FORBIDDEN);
        }
        if (!newLoan.getPayments().contains(loanApplicationDTO.getPayments())) {
            return new ResponseEntity<>("Payments requested exceeds the maximum of the loan", HttpStatus.FORBIDDEN);
        }
        if (destinyAccount == null) {
            return new ResponseEntity<>("Destiny account doesn't exist", HttpStatus.FORBIDDEN);
        }
        if (!client.getAccounts().contains(destinyAccount)) {
            return new ResponseEntity<>("The account doesn't correspond to the client", HttpStatus.FORBIDDEN);
        }

        Double loanRevenue = (loanApplicationDTO.getAmount() * 1.20);

        ClientLoan newClientLoan = new ClientLoan(loanRevenue, loanApplicationDTO.getPayments());
        Transaction newTransaction = new Transaction(TransactionType.CREDIT, loanRevenue, newLoan.getName() + " - Loan approved", LocalDateTime.now());
        destinyAccount.setBalance(destinyAccount.getBalance() + loanApplicationDTO.getAmount());
        destinyAccount.addTransaction(newTransaction);

        client.addClientLoan(newClientLoan);
        newLoan.addClientLoan(newClientLoan);
        clientLoanService.saveClientLoan(newClientLoan);
        transactionService.saveTransaction(newTransaction);
        loanService.saveLoan(newLoan);
        clientService.saveClient(client);


        return new ResponseEntity<>("Loan applied correctly", HttpStatus.CREATED);
    }
}