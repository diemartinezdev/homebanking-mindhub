package com.mindhub.homebanking.services;

import com.mindhub.homebanking.dtos.AccountDTO;
import com.mindhub.homebanking.models.Account;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

public interface AccountService {
    List<AccountDTO> getAccounts();
    AccountDTO getAccounts(@PathVariable Long id);
    List<AccountDTO> getCurrentClientAccounts(Authentication authentication);
    void saveAccount(Account account);
    Account findByNumber(String number);
}
