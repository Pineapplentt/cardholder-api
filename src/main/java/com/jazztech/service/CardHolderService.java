package com.jazztech.service;

import com.jazztech.cardholder.CreditAnalysisClient;
import com.jazztech.cardholder.dto.AnalysisSearch;
import com.jazztech.controller.request.cardholder.CardHolderRequest;
import com.jazztech.controller.response.cardholder.CardHolderResponse;
import com.jazztech.mapper.bankaccount.BankAccountModelToEntityMapper;
import com.jazztech.mapper.cardholder.CardHolderEntityToResponseMapper;
import com.jazztech.mapper.cardholder.CardHolderModelToEntityMapper;
import com.jazztech.mapper.cardholder.CardHolderRequestToModelMapper;
import com.jazztech.model.CardHolderModel;
import com.jazztech.repository.CardHolderRepository;
import com.jazztech.repository.entity.BankAccount;
import com.jazztech.repository.entity.CardHolderEntity;
import com.jazztech.utils.CardHolderStatus;
import java.util.List;
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
//        try {
            final AnalysisSearch analysis = getAnalysis(UUID.fromString(cardHolderRequest.creditAnalysisId()));
            final BankAccount bankAccount = cardHolderRequest.bankAccountEntity();
            return CardHolderModel.builder()
                    .cardHolderId(analysis.clientId())
                    .creditAnalysisId(analysis.analysisId())
                    .bankAccount(bankAccount)
                    .status(CardHolderStatus.ACTIVE)
                    .limit(analysis.approvedLimit())
                    .build();
//        } catch (FeignException exception) {
//            throw new RuntimeException("Credit analysis not found");
//        }
    }

    public CardHolderEntity saveCardHolder(CardHolderEntity cardHolderEntity) {
        return cardHolderRepository.save(cardHolderEntity);
    }

    public AnalysisSearch getAnalysis(UUID analysisId) {
        return creditAnalysisApi.getAllAnalysis(analysisId);
    }

    public List<CardHolderEntity> getAllCardHolders() {
        return cardHolderRepository.findAll();
    }

    public CardHolderResponse getCardHolderById(UUID id) {
        return cardHolderEntityToResponseMapper.from(cardHolderRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Card holder not found")));
    }
}
