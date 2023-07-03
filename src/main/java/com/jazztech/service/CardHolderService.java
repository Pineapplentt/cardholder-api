package com.jazztech.service;

import com.jazztech.cardholder.CreditAnalysisClient;
import com.jazztech.cardholder.dto.AnalysisSearch;
import com.jazztech.controller.request.cardholder.CardHolderRequest;
import com.jazztech.controller.response.cardholder.CardHolderResponse;
import com.jazztech.exception.AnalysisApiConnectionException;
import com.jazztech.exception.AnalysisNotFoundException;
import com.jazztech.exception.CardHolderAlreadyExistsException;
import com.jazztech.exception.CardHolderNotFoundException;
import com.jazztech.exception.CustomIllegalArgumentException;
import com.jazztech.exception.InvalidStatusException;
import com.jazztech.mapper.bankaccount.BankAccountModelToEntityMapper;
import com.jazztech.mapper.cardholder.CardHolderEntityToResponseMapper;
import com.jazztech.mapper.cardholder.CardHolderModelToEntityMapper;
import com.jazztech.mapper.cardholder.CardHolderRequestToModelMapper;
import com.jazztech.model.CardHolderModel;
import com.jazztech.repository.CardHolderRepository;
import com.jazztech.repository.entity.BankAccount;
import com.jazztech.repository.entity.CardHolderEntity;
import com.jazztech.utils.CardHolderStatus;
import feign.FeignException;
import feign.RetryableException;
import java.util.List;
import java.util.Objects;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CardHolderService {
    private final CreditAnalysisClient creditAnalysisApi;
    private final CardHolderRepository cardHolderRepository;
    private final CardHolderRequestToModelMapper cardHolderRequestToModelMapper;
    private final CardHolderModelToEntityMapper cardHolderModelToEntityMapper;
    private final CardHolderEntityToResponseMapper cardHolderEntityToResponseMapper;
    private final BankAccountModelToEntityMapper bankAccountModelToEntityMapper;

    public CardHolderResponse createCardHolder(CardHolderRequest cardHolderRequest) {
        final CardHolderEntity cardHolderEntity = cardHolderModelToEntityMapper.from(cardHolderBuilder(cardHolderRequest));
        final CardHolderEntity savedCardHolderEntity = saveCardHolder(cardHolderEntity);
        return cardHolderEntityToResponseMapper.from(saveCardHolder(savedCardHolderEntity));
    }

    public CardHolderModel cardHolderBuilder(CardHolderRequest cardHolderRequest) {

        final List<CardHolderEntity> cardHolderEntities = cardHolderRepository.findAll();
        if (cardHolderEntities.stream()
                .anyMatch(cardHolderEntity -> cardHolderEntity.getClientId()
                        .equals(UUID.fromString(cardHolderRequest.clientId())))) {
            throw new CardHolderAlreadyExistsException("Card Holder already registered, check the data sent for registration and try again");
        }

        try {
            final AnalysisSearch analysis = getAnalysis(UUID.fromString(cardHolderRequest.creditAnalysisId()));
            final BankAccount bankAccount = cardHolderRequest.bankAccount();
            return CardHolderModel.builder()
                    .id(analysis.clientId())
                    .clientId(UUID.fromString(cardHolderRequest.clientId()))
                    .creditAnalysisId(analysis.idAnalysis())
                    .bankAccount(bankAccount)
                    .status(CardHolderStatus.ACTIVE)
                    .limit(analysis.approvedLimit())
                    .availableLimit(analysis.approvedLimit())
                    .build();
        } catch (FeignException.NotFound exception) {
            throw new AnalysisNotFoundException("Credit analysis and/or client not found, check if both of ids correspond to the same client");
        } catch (RetryableException exception) {
            throw new AnalysisApiConnectionException("Error connecting to analysis API, the service is unavailable or the request timed out");
        } catch (IllegalArgumentException exception) {
            throw new CustomIllegalArgumentException("Invalid UUID format, please use the following format: xxxxxxxx-xxxx-xxxx-xxxx-xxxxxxxxxxxx");
        }
    }

    public CardHolderEntity saveCardHolder(CardHolderEntity cardHolderEntity) {
        return cardHolderRepository.save(cardHolderEntity);
    }

    public AnalysisSearch getAnalysis(UUID analysisId) {
        return creditAnalysisApi.getAllAnalysis(analysisId);
    }

    public List<CardHolderResponse> getAllCardHolders(String status) {
        try {
            if (Objects.isNull(status)) {
                final List<CardHolderEntity> cardHolderEntities = cardHolderRepository.findAll();
                return cardHolderEntities.stream().map(cardHolderEntityToResponseMapper::from).toList();
            }
            final List<CardHolderEntity> cardHolderEntities = cardHolderRepository.findByStatus(CardHolderStatus.valueOf(status.toUpperCase()));
            return cardHolderEntities.stream().map(cardHolderEntityToResponseMapper::from).toList();
        } catch (IllegalArgumentException e) {
            throw new InvalidStatusException("Invalid status, please use ACTIVE or INACTIVE");
        }
    }
}
