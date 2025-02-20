package com.jazztech.service;

import com.jazztech.controller.request.card.CardRequest;
import com.jazztech.controller.request.card.LimitUpdateRequest;
import com.jazztech.controller.response.card.CardResponse;
import com.jazztech.controller.response.card.LimitUpdateResponse;
import com.jazztech.exception.CardHolderNotFoundException;
import com.jazztech.exception.CardNotFoundException;
import com.jazztech.exception.InactiveCardHolderException;
import com.jazztech.exception.InsufficientLimitException;
import com.jazztech.exception.InvalidLimitException;
import com.jazztech.mapper.card.CardEntityToLimitUpdateResponseMapper;
import com.jazztech.mapper.card.CardEntityToModelMapper;
import com.jazztech.mapper.card.CardEntityToResponseMapper;
import com.jazztech.mapper.card.CardHolderEntityToIdMapper;
import com.jazztech.mapper.card.CardModelToEntityMapper;
import com.jazztech.mapper.cardholder.CardHolderEntityToModelMapper;
import com.jazztech.mapper.cardholder.CardHolderModelToEntityMapper;
import com.jazztech.model.CardModel;
import com.jazztech.repository.CardHolderRepository;
import com.jazztech.repository.CardRepository;
import com.jazztech.repository.entity.CardEntity;
import com.jazztech.repository.entity.CardHolderEntity;
import com.jazztech.utils.CardHolderStatus;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.ThreadLocalRandom;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CardService {
    private static final String CARD_NUMBER_VISA_PREFIX = "4";
    private static final Integer CARD_NUMBER_LENGTH = 15;
    private static final String CARD_NOT_FOUND_MESSAGE = "Card not found, check the card id then try again";
    private static final String CARD_HOLDER_NOT_FOUND_MESSAGE = "Card holder not found, check the card holder id and try again";
    private final CardRepository cardRepository;
    private final CardHolderService cardHolderService;
    private final CardModelToEntityMapper cardModelToEntityMapper;
    private final CardEntityToResponseMapper cardEntityToResponseMapper;
    private final CardHolderRepository cardHolderRepository;
    private final CardHolderEntityToIdMapper cardHolderEntityToIdMapper;
    private final CardEntityToLimitUpdateResponseMapper cardEntityToLimitUpdateResponseMapper;
    private final CardHolderEntityToModelMapper cardHolderEntityToModelMapper;
    private final CardHolderModelToEntityMapper cardHolderModelToEntityMapper;
    private final CardEntityToModelMapper cardEntityToModelMapper;

    public CardResponse createCard(UUID cardHolderId, CardRequest cardRequest) {
        final CardHolderEntity cardHolder = cardHolderRepository.findById(cardHolderId)
                .orElseThrow(() -> new CardHolderNotFoundException(CARD_HOLDER_NOT_FOUND_MESSAGE));
        final CardEntity cardEntity = cardModelToEntityMapper.from(cardBuilder(cardHolder, cardRequest));

        final CardEntity savedCardEntity = saveCardEntity(cardEntity.toBuilder().cardHolderId(cardHolder).build());
        return cardEntityToResponseMapper.from(savedCardEntity);
    }

    public CardModel cardBuilder(CardHolderEntity cardHolder, CardRequest cardRequest) {

        if (cardRequest.limit().compareTo(BigDecimal.ZERO) <= 0) {
            throw new InvalidLimitException("Limit request must be greater than zero");
        }

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

        final BigDecimal newAvailableLimit = cardHolder.getAvailableLimit().subtract(cardRequest.limit());

        cardHolderRepository.updateCardHolderAvailableLimit(cardHolder.getId(), newAvailableLimit);

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
        final CardHolderEntity cardHolderEntity = cardHolderRepository.findById(cardHolderId)
                .orElseThrow(() -> new CardHolderNotFoundException(CARD_HOLDER_NOT_FOUND_MESSAGE));
        final List<CardEntity> cardEntities = cardRepository.findByCardHolderId(cardHolderEntity);
        return cardEntities.stream().map(cardEntityToResponseMapper::from).toList();
    }

    public CardResponse getCardById(UUID cardHolderId, UUID cardId) {
        final CardHolderEntity cardHolderEntity = cardHolderRepository.findById(cardHolderId)
                .orElseThrow(() -> new CardHolderNotFoundException(CARD_HOLDER_NOT_FOUND_MESSAGE));
        return cardEntityToResponseMapper.from(cardRepository.findByCardHolderIdAndCardId(cardHolderEntity, cardId)
                .orElseThrow(() -> new CardNotFoundException(CARD_NOT_FOUND_MESSAGE)));
    }

    public LimitUpdateResponse updateCard(UUID cardHolderId, UUID cardId, LimitUpdateRequest limitUpdateRequest) {

        if (limitUpdateRequest.limit().compareTo(BigDecimal.ZERO) <= 0) {
            throw new InvalidLimitException("Limit update request must be greater than zero");
        }

        final CardHolderEntity cardHolderEntity = cardHolderRepository.findById(cardHolderId)
                .orElseThrow(() -> new CardHolderNotFoundException(CARD_HOLDER_NOT_FOUND_MESSAGE));

        final CardEntity cardEntity = cardRepository.findByCardHolderIdAndCardId(cardHolderEntity, cardId)
                .orElseThrow(() -> new CardNotFoundException(CARD_NOT_FOUND_MESSAGE));

        if (limitUpdateRequest.limit().compareTo(cardEntity.getLimit()) == 0) {
            throw new InvalidLimitException("Limit update request must be different than the current limit");
        }

        final BigDecimal limitDifference = cardHolderEntity.getAvailableLimit().subtract(limitUpdateRequest.limit());
        final BigDecimal cardLimitAfterCalculation = cardEntity.getLimit().subtract(limitUpdateRequest.limit());


        // if limit update request is less than the current limit
        if (limitUpdateRequest.limit().compareTo(cardEntity.getLimit()) < 0) {
            final BigDecimal finalAddLimitValue = cardHolderEntity.getAvailableLimit().add(cardLimitAfterCalculation);

            cardHolderRepository.updateCardHolderAvailableLimit(cardHolderId, finalAddLimitValue);
            cardRepository.updateCardLimit(cardId, limitUpdateRequest.limit());

            final CardEntity updatedCardEntity = cardRepository.findByCardHolderIdAndCardId(cardHolderEntity, cardId)
                    .orElseThrow(() -> new CardNotFoundException(CARD_NOT_FOUND_MESSAGE));

            return Optional.of(cardEntityToLimitUpdateResponseMapper.from(updatedCardEntity))
                    .orElseThrow(() -> new CardNotFoundException(CARD_NOT_FOUND_MESSAGE));
        }

        if (limitDifference.compareTo(BigDecimal.ZERO) < 0) {
            throw new InsufficientLimitException(
                    "Card holder has insufficient limit, in order to update a credit card, the card holder must have a limit greater than zero"
                            + " and greater than the requested limit");
        }

        // if limit update request is greater than the current limit
        final BigDecimal calculation = limitUpdateRequest.limit().subtract(cardEntity.getLimit());
        final BigDecimal finalSubtractLimitValue = cardHolderEntity.getAvailableLimit().subtract(calculation);

        cardHolderRepository.updateCardHolderAvailableLimit(cardHolderId, finalSubtractLimitValue);
        cardRepository.updateCardLimit(cardId, limitUpdateRequest.limit());

        final CardEntity updatedCardEntity = cardRepository.findByCardHolderIdAndCardId(cardHolderEntity, cardId)
                .orElseThrow(() -> new CardNotFoundException(CARD_NOT_FOUND_MESSAGE));

        return Optional.of(cardEntityToLimitUpdateResponseMapper.from(updatedCardEntity))
                .orElseThrow(() -> new CardNotFoundException(CARD_NOT_FOUND_MESSAGE));
    }
}
