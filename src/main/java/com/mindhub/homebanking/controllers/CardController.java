package com.mindhub.homebanking.controllers;

import com.mindhub.homebanking.dtos.CardDTO;
import com.mindhub.homebanking.models.*;
import com.mindhub.homebanking.repositories.CardRepository;
import com.mindhub.homebanking.repositories.ClientRepository;
import com.mindhub.homebanking.utilities.Utility;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

import static java.util.stream.Collectors.toList;

@RestController
@RequestMapping("/api")
public class CardController {

    @Autowired
    private CardRepository cardRepository;
    @Autowired
    private ClientRepository clientRepository;

    @RequestMapping("/cards")
    public List<CardDTO> getCards() {
        return cardRepository.findAll().stream().map(card -> new CardDTO(card)).collect(toList());
    }

    @RequestMapping("/cards/{id}")
    public CardDTO getCards(@PathVariable Long id) {
        return new CardDTO(cardRepository.findById(id).orElse(null));
    }

    @RequestMapping(path = "/clients/current/cards", method = RequestMethod.POST)
    public ResponseEntity<Object> create(@RequestParam CardType cardType, @RequestParam CardColor cardColor, Authentication authentication) {
        Client newClient = clientRepository.findByEmail(authentication.getName());
        if (newClient.getCards().stream().filter(card -> card.getType().equals(cardType)).count() > 2) {
            return new ResponseEntity<>("Already have 3 cards of that type", HttpStatus.FORBIDDEN);
        }
        Card newCard = new Card(newClient.getFirstName() + " " + newClient.getLastName(), cardType, cardColor, Utility.getCardNumber(), Utility.getCvvNumber(), LocalDate.now(), LocalDate.now().plusYears(5));
        Client clientCard = clientRepository.findByEmail(authentication.getName());
        clientCard.addCard(newCard);
        cardRepository.save(newCard);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }
}
