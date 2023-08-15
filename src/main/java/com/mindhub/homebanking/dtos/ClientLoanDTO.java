package com.mindhub.homebanking.dtos;

import com.mindhub.homebanking.models.ClientLoan;

import java.util.ArrayList;
import java.util.List;

public class ClientLoanDTO {
    private Long id;
    private Double amount;
    private String name;
    private List<Integer> payments = new ArrayList<>();
    private Long loanId;


    public ClientLoanDTO(ClientLoan clientLoan) {
        this.id = clientLoan.getId();
        this.amount = clientLoan.getAmount();
        this.name = clientLoan.getLoan().getName();
        this.payments = clientLoan.getPayments();
        this.loanId = clientLoan.getLoan().getId();
    }

    public Long getId() {
        return id;
    }

    public Double getAmount() {
        return amount;
    }

    public String getName() {
        return name;
    }

    public List<Integer> getPayments() {
        return payments;
    }

    public Long getLoanId() {
        return loanId;
    }
}
