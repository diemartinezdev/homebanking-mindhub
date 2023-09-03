package com.mindhub.homebanking.dtos;

public class LoanApplicationDTO {
    private String loanId;
    private Double amount;
    private Integer payments;
    private String destinationAccount;

    public LoanApplicationDTO() { }

    public LoanApplicationDTO(String loanId, Double amount, Integer payments, String destinationAccount) {
        this.loanId = loanId;
        this.amount = amount;
        this.payments = payments;
        this.destinationAccount = destinationAccount;
    }

    public String getLoanId() {
        return loanId;
    }

    public Double getAmount() {
        return amount;
    }

    public Integer getPayments() {
        return payments;
    }

    public String getDestinationAccount() {
        return destinationAccount;
    }
}
