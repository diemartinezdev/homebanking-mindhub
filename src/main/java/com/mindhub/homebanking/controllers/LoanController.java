package com.mindhub.homebanking.controllers;

import com.mindhub.homebanking.dtos.LoanApplicationDTO;
import com.mindhub.homebanking.dtos.LoanDTO;
import com.mindhub.homebanking.models.Account;
import com.mindhub.homebanking.models.Client;
import com.mindhub.homebanking.models.Loan;
import com.mindhub.homebanking.repositories.AccountRepository;
import com.mindhub.homebanking.repositories.ClientRepository;
import com.mindhub.homebanking.repositories.LoanRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import javax.transaction.Transactional;
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
    }
}
