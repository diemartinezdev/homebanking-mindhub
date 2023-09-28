package com.mindhub.homebanking.services.implement;

import com.mindhub.homebanking.dtos.CardDTO;
import com.mindhub.homebanking.models.Card;
import com.mindhub.homebanking.models.Client;
import com.mindhub.homebanking.repositories.CardRepository;
import com.mindhub.homebanking.services.CardService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import org.springframework.security.access.AccessDeniedException;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class CardServiceImplement implements CardService {
    @Autowired
    private CardRepository cardRepository;

    @Override
    public List<CardDTO> getCards() {
        return cardRepository.findAll().stream().map(card -> new CardDTO(card)).collect(Collectors.toList());
    }
    @Override
    @Transactional
    public void deleteCardById(Long id){
        cardRepository.deleteById(id);
    }

    @Override
    public void saveCard(Card card) {
        cardRepository.save(card);
    }

    @Override
    public Card findById(Long id) {
        return cardRepository.findById(id).orElse(null);
    }
}
