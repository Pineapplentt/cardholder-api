package com.jazztech.service;

import com.jazztech.controller.request.card.CardRequest;
import com.jazztech.controller.response.card.CardListResponse;
import com.jazztech.controller.response.card.CardResponse;
import com.jazztech.mapper.card.CardEntityToResponseMapper;
import com.jazztech.mapper.card.CardModelToEntityMapper;
import com.jazztech.mapper.card.CardRequestToModelMapper;
import com.jazztech.model.CardModel;
import com.jazztech.repository.CardRepository;
import com.jazztech.repository.entity.CardEntity;
import java.time.LocalDate;
import java.util.List;
import java.util.Random;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CardService {
    private final CardRepository cardRepository;
    private final CardRequestToModelMapper cardHolderRequestToModelMapper;
    private final CardModelToEntityMapper cardModelToEntityMapper;
    private final CardEntityToResponseMapper cardEntityToResponseMapper;
    private final Random random = new Random();

    public CardResponse createCard(CardRequest cardRequest) {
        try {
            final CardEntity cardEntity = cardModelToEntityMapper.from(cardBuilder(cardRequest));
            final CardEntity savedCardEntity = saveCardEntity(cardEntity);
            return cardEntityToResponseMapper.from(savedCardEntity);
        } catch (Exception e) {
            // TODO log exception
            throw new RuntimeException(e);
        }
    }

    public CardModel cardBuilder(CardRequest cardRequest) {
        StringBuilder cardNumber = new StringBuilder();

        // IIN (Issuer Identification Number) for Visa
        cardNumber.append("4");

        // Generate random digits for the card number
        for (int i = 0; i < 15; i++) {
            int digit = random.nextInt(10);
            cardNumber.append(digit);
        }

        // Generate random digits for the CVV
        Integer cvv = random.nextInt(1000);

        // Generate random digits for the due date
        LocalDate dueDate = LocalDate.now().plusMonths(3).plusYears(5);

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
        List<CardEntity> cardEntities = cardRepository.findByCardHolderId(cardHolderId);
        return cardEntities.stream().map(cardEntityToResponseMapper::from).toList();
    }

    public CardResponse getCardById(UUID cardId) {
        return cardRepository.findById(cardId)
                .map(cardEntityToResponseMapper::from)
                .orElseThrow(() -> new RuntimeException("Card not found"));
    }
}
