package com.jazztech.service;

import com.jazztech.controller.request.card.CardRequest;
import com.jazztech.controller.request.card.LimitUpdateRequest;
import com.jazztech.controller.response.card.CardResponse;
import com.jazztech.controller.response.card.LimitUpdateResponse;
import com.jazztech.mapper.card.CardEntityToResponseMapper;
import com.jazztech.mapper.card.CardModelToEntityMapper;
import com.jazztech.model.CardModel;
import com.jazztech.repository.CardRepository;
import com.jazztech.repository.entity.CardEntity;
import java.time.LocalDate;
import java.util.List;
import java.util.Random;
import java.util.UUID;
import java.util.concurrent.ThreadLocalRandom;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CardService {
    private final CardRepository cardRepository;
    private final CardModelToEntityMapper cardModelToEntityMapper;
    private final CardEntityToResponseMapper cardEntityToResponseMapper;
    static final String CARD_NUMBER_VISA_PREFIX = "4";
    static final Integer CARD_NUMBER_LENGTH = 15;

    public CardResponse createCard(CardRequest cardRequest) {
        final CardEntity cardEntity = cardModelToEntityMapper.from(cardBuilder(cardRequest));
        final CardEntity savedCardEntity = saveCardEntity(cardEntity);
        return cardEntityToResponseMapper.from(savedCardEntity);
    }

    public CardModel cardBuilder(CardRequest cardRequest) {
        final StringBuilder cardNumber = new StringBuilder();

        // IIN (Issuer Identification Number) for Visa
        cardNumber.append(CARD_NUMBER_VISA_PREFIX);

        // Generate random digits for the card number
        for (int i = 0; i < CARD_NUMBER_LENGTH; i++) {
            final int digit = ThreadLocalRandom.current().nextInt(0, 10);
            cardNumber.append(digit);
        }

        // Generate random digits for the CVV
        final Integer cvv = ThreadLocalRandom.current().nextInt(100, 1000);

        // Generate random digits for the due date
        final LocalDate dueDate = LocalDate.now().plusMonths(3).plusYears(5);

        return CardModel.builder()
                .cardHolderId(UUID.fromString(cardRequest.cardHolderId()))
                .cardId(UUID.randomUUID())
                .cardNumber(cardNumber.toString())
                .limit(cardRequest.limit())
                .cvv(cvv)
                .dueDate(dueDate)
                .build();
    }

    public CardEntity saveCardEntity(CardEntity cardEntity) {
        return cardRepository.save(cardEntity);
    }

    public List<CardResponse> getAllCards(UUID cardHolderId) {
        final List<CardEntity> cardEntities = cardRepository.findByCardHolderId(cardHolderId);
        return cardEntities.stream().map(cardEntityToResponseMapper::from).toList();
    }

    public CardResponse getCardById(UUID cardId) {
        return cardRepository.findById(cardId)
                .map(cardEntityToResponseMapper::from)
                .orElseThrow(() -> new RuntimeException("Card not found"));
    }

    public LimitUpdateResponse updateCard(UUID id, LimitUpdateRequest limitUpdateRequest) {
        return null;
    }
}
