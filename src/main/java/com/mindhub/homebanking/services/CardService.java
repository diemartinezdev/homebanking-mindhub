package com.mindhub.homebanking.services;

import com.mindhub.homebanking.dtos.CardDTO;
import com.mindhub.homebanking.models.Card;
import com.mindhub.homebanking.models.Client;

import javax.persistence.EntityNotFoundException;
import java.util.List;
import java.util.Optional;

public interface CardService {
    List<CardDTO> getCards();
    void deleteCardById(Long id);
    void saveCard(Card card);
    Card findById(Long id);
}
