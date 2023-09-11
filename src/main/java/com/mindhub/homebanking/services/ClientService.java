package com.mindhub.homebanking.services;

import com.mindhub.homebanking.dtos.ClientDTO;
import com.mindhub.homebanking.models.Client;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

public interface ClientService {
    List<ClientDTO> getClients();
    ClientDTO getCurrent(Authentication authentication);
    Client findByEmail(String email);
    ClientDTO getClients(@PathVariable Long id);
    void saveClient(Client client);
}
