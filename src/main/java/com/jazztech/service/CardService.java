package com.jazztech.service;

import com.jazztech.controller.request.card.CardRequest;
import com.jazztech.controller.response.card.CardResponse;
import com.jazztech.exception.InactiveCardHolderException;
import com.jazztech.exception.InsufficientLimitException;
import com.jazztech.mapper.card.CardEntityToResponseMapper;
import com.jazztech.mapper.card.CardHolderEntityToIdMapper;
import com.jazztech.mapper.card.CardModelToEntityMapper;
import com.jazztech.model.CardModel;
import com.jazztech.repository.CardHolderRepository;
import com.jazztech.repository.CardRepository;
import com.jazztech.repository.entity.CardEntity;
import com.jazztech.repository.entity.CardHolderEntity;
import com.jazztech.utils.CardHolderStatus;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.ThreadLocalRandom;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CardService {
    private static final String CARD_NUMBER_VISA_PREFIX = "4";
    private static final Integer CARD_NUMBER_LENGTH = 15;
    private final CardRepository cardRepository;
    private final CardModelToEntityMapper cardModelToEntityMapper;
    private final CardEntityToResponseMapper cardEntityToResponseMapper;
    private final CardHolderRepository cardHolderRepository;
    private final CardHolderEntityToIdMapper cardHolderEntityToIdMapper;

    public CardResponse createCard(UUID cardHolderId, CardRequest cardRequest) {
        final CardHolderEntity cardHolder = cardHolderRepository.findById(cardHolderId).get();
        final CardEntity cardEntity = cardModelToEntityMapper.from(cardBuilder(cardHolder, cardRequest));

        final CardEntity savedCardEntity = saveCardEntity(cardEntity.toBuilder().cardHolderId(cardHolder).build());
        return cardEntityToResponseMapper.from(savedCardEntity);
    }

    public CardModel cardBuilder(CardHolderEntity cardHolder, CardRequest cardRequest) {
        if (cardHolder.getAvailableLimit().compareTo(BigDecimal.ZERO) <= 0 || cardHolder.getAvailableLimit().compareTo(cardRequest.limit()) < 0) {
            throw new InsufficientLimitException(
                    "Card holder has insufficient limit, in order to create a credit card, the card holder must have a limit greater than zero"
                            + " and greater than the requested limit");
        }
        if (cardHolder.getStatus().equals(CardHolderStatus.INACTIVE)) {
            throw new InactiveCardHolderException("Card holder is inactive, in order to create a credit card, the card holder must be active");
        }

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

        CardHolderEntity cardHolderEntity = cardHolderRepository.findById(cardHolder.getId()).get();
        cardHolderEntity = cardHolderEntity.toBuilder()
                .availableLimit(cardHolderEntity.getAvailableLimit().subtract(cardRequest.limit()))
                .build();
        cardHolderRepository.save(cardHolderEntity);

        return CardModel.builder()
                .cardId(UUID.randomUUID())
                .cardHolderId(cardHolder.getId())
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
        final CardHolderEntity cardHolderEntity = cardHolderRepository.findById(cardHolderId).get();
        final List<CardEntity> cardEntities = cardRepository.findByCardHolderId(cardHolderEntity);
        return cardEntities.stream().map(cardEntityToResponseMapper::from).toList();
    }
}
