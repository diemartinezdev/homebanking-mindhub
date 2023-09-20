package com.mindhub.homebanking.controllers;

import com.mindhub.homebanking.dtos.CardDTO;
import com.mindhub.homebanking.models.*;
import com.mindhub.homebanking.repositories.CardRepository;
import com.mindhub.homebanking.repositories.ClientRepository;
import com.mindhub.homebanking.services.CardService;
import com.mindhub.homebanking.services.ClientService;
import com.mindhub.homebanking.utilities.Utility;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import javax.persistence.EntityNotFoundException;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static java.util.stream.Collectors.toList;

@RestController
@RequestMapping("/api")
public class CardController {
    @Autowired
    private CardService cardService;
    @Autowired
    private ClientService clientService;

    @GetMapping("/cards")
    public List<CardDTO> getCards() {
        return cardService.getCards();
    }


    @PostMapping("/clients/current/cards")
    public ResponseEntity<Object> create(@RequestParam CardType cardType, @RequestParam CardColor cardColor, Authentication authentication) {
        Client newClient = clientService.findByEmail(authentication.getName());
        if (newClient.getCards().stream().filter(card -> card.getType().equals(cardType)).count() > 2) {
            return new ResponseEntity<>("Already have 3 cards of that type", HttpStatus.FORBIDDEN);
        }
        Card newCard = new Card(newClient.getFirstName() + " " + newClient.getLastName(), cardType, cardColor, Utility.getCardNumber(), Utility.getCvvNumber(), LocalDate.now(), LocalDate.now().plusYears(5),true);
        Client clientCard = clientService.findByEmail(authentication.getName());
        clientCard.addCard(newCard);
        cardService.saveCard(newCard);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @DeleteMapping("/cards/{id}")
    public ResponseEntity<Long> deleteCardById(@PathVariable Long id) {
        try {
            Optional<Card> cardOptional = cardService.findById(id);
            if (cardOptional.isPresent()) {
                Card card = cardOptional.get();

                card.setActive(false); // Desactiva la tarjeta
                cardService.saveCard(card);

                return new ResponseEntity<>(HttpStatus.OK);
            } else {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

}
