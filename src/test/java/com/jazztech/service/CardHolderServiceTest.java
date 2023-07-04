package com.jazztech.service;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.empty;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.everyItem;

import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.hasProperty;
import static org.hamcrest.Matchers.instanceOf;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.not;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

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
import com.jazztech.mapper.cardholder.CardHolderEntityToResponseMapper;
import com.jazztech.mapper.cardholder.CardHolderEntityToResponseMapperImpl;
import com.jazztech.mapper.cardholder.CardHolderModelToEntityMapper;
import com.jazztech.mapper.cardholder.CardHolderModelToEntityMapperImpl;
import com.jazztech.mapper.cardholder.CardHolderRequestToModelMapper;
import com.jazztech.mapper.cardholder.CardHolderRequestToModelMapperImpl;
import com.jazztech.repository.CardHolderRepository;
import com.jazztech.repository.entity.BankAccount;
import com.jazztech.repository.entity.CardHolderEntity;
import com.jazztech.utils.CardHolderStatus;
import feign.FeignException;
import feign.RetryableException;
import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
class CardHolderServiceTest {
    @Spy
    CardHolderRequestToModelMapper cardHolderRequestToModelMapper = new CardHolderRequestToModelMapperImpl();
    @Spy
    CardHolderModelToEntityMapper cardHolderModelToEntityMapper = new CardHolderModelToEntityMapperImpl();
    @Spy
    CardHolderEntityToResponseMapper cardHolderEntityToResponseMapper = new CardHolderEntityToResponseMapperImpl();
    @Mock
    private CreditAnalysisClient creditAnalysisApi;
    @Mock
    private CardHolderRepository cardHolderRepository;
    @InjectMocks
    private CardHolderService cardHolderService;

    @Captor
    private ArgumentCaptor<CardHolderRequest> cardHolderRequestArgumentCaptor;

    @Captor
    private ArgumentCaptor<CardHolderEntity> cardHolderEntityArgumentCaptor;

    @Captor
    private ArgumentCaptor<UUID> uuidArgumentCaptor;

    //factory
    public static CardHolderRequest cardHolderRequestFactory() {
        return CardHolderRequest.builder()
                .clientId(UUID.fromString("c0a8a4d0-8e1f-4b7a-9b1a-8e9a9a9a9a9a"))
                .creditAnalysisId(UUID.fromString("c0a8a4d0-8e1f-4b7a-9b1a-8e9a9a9a9a8a"))
                .bankAccount(bankAccountFactory())
                .build();
    }

    public static CardHolderEntity activeCardHolderEntityFactory() {
        return CardHolderEntity.builder()
                .id(UUID.randomUUID())
                .clientId(cardHolderRequestFactory().clientId())
                .creditAnalysisId(cardHolderRequestFactory().creditAnalysisId())
                .bankAccount(bankAccountFactory())
                .status(CardHolderStatus.ACTIVE)
                .limit(BigDecimal.valueOf(1000.0))
                .build();
    }

    public static CardHolderEntity inactiveCardHolderEntityFactory() {
        return CardHolderEntity.builder()
                .id(UUID.randomUUID())
                .clientId(cardHolderRequestFactory().clientId())
                .creditAnalysisId(cardHolderRequestFactory().creditAnalysisId())
                .bankAccount(bankAccountFactory())
                .status(CardHolderStatus.INACTIVE)
                .limit(BigDecimal.valueOf(1000.0))
                .build();
    }

    public static BankAccount bankAccountFactory() {
        return BankAccount.builder()
                .bankAccountId(UUID.fromString("c0a8a4d0-8e1f-4b7a-9b1a-8e9a9a9a9a9a"))
                .account("27184771-6")
                .agency("1121")
                .bankCode("302")
                .build();
    }

    public static AnalysisSearch analysisSearchFactory() {
        return AnalysisSearch.builder()
                .idAnalysis(UUID.fromString("c0a8a4d0-8e1f-4b7a-9b1a-8e9a9a9a9a8a"))
                .clientId(UUID.fromString("c0a8a4d0-8e1f-4b7a-9b1a-8e9a9a9a9a9a"))
                .approvedLimit(BigDecimal.valueOf(1000.0))
                .build();
    }

    public static List<CardHolderEntity> activeCardHolderEntityListFactory() {
        return List.of(activeCardHolderEntityFactory(), activeCardHolderEntityFactory(), activeCardHolderEntityFactory());
    }

    public static List<CardHolderEntity> inactiveCardHolderEntityListFactory() {
        return List.of(inactiveCardHolderEntityFactory(), inactiveCardHolderEntityFactory(), inactiveCardHolderEntityFactory());
    }

    public static List<CardHolderEntity> mixedCardHolderEntityListFactory() {
        return List.of(activeCardHolderEntityFactory(), inactiveCardHolderEntityFactory(), activeCardHolderEntityFactory());
    }

    @Test
    void should_create_card_holder() {
        when(cardHolderRepository.save(cardHolderEntityArgumentCaptor.capture())).thenReturn(activeCardHolderEntityFactory());
        when(creditAnalysisApi.getAnalysisById(uuidArgumentCaptor.capture())).thenReturn(analysisSearchFactory());

        final CardHolderRequest cardHolderRequest = cardHolderRequestFactory();
        final CardHolderResponse cardHolderResponse = cardHolderService.createCardHolder(cardHolderRequest);

        cardHolderService.createCardHolder(cardHolderRequest);

        assertNotNull(cardHolderResponse);
        final UUID cardHolderId = cardHolderEntityArgumentCaptor.getValue().getClientId();
        assertEquals(cardHolderRequest.clientId(), cardHolderId);
    }

    @Test
    void should_throw_card_holder_already_exists_exception_when_card_holder_already_exists() {
        when(cardHolderRepository.findById(uuidArgumentCaptor.capture())).thenReturn(Optional.of(activeCardHolderEntityFactory()));

        assertThrows(CardHolderAlreadyExistsException.class, () -> cardHolderService.createCardHolder(cardHolderRequestFactory()));
    }

    @Test
    void should_throw_analysis_not_found_exception_when_analysis_not_found() {
        when(cardHolderService.getAnalysis(uuidArgumentCaptor.capture())).thenThrow(FeignException.NotFound.class);

        assertThrows(AnalysisNotFoundException.class, () -> cardHolderService.createCardHolder(cardHolderRequestFactory()));
    }

    @Test
    void should_throw_analysis_api_connection_exception_when_analysis_api_connection_fails_when_creating_card_holder() {
        when(cardHolderService.getAnalysis(uuidArgumentCaptor.capture())).thenThrow(RetryableException.class);

        assertThrows(AnalysisApiConnectionException.class, () -> cardHolderService.createCardHolder(cardHolderRequestFactory()));
    }

    @Test
    void should_throw_custom_illegal_argument_exception_when_uuid_is_out_of_format() {
        when(cardHolderService.getAnalysis(uuidArgumentCaptor.capture())).thenThrow(IllegalArgumentException.class);

        assertThrows(CustomIllegalArgumentException.class, () -> cardHolderService.createCardHolder(cardHolderRequestFactory()));
    }

    @Test
    void should_return_list_of_all_card_holders_when_status_is_null() {
        when(cardHolderRepository.findAll()).thenReturn(mixedCardHolderEntityListFactory());

        List<CardHolderResponse> list = cardHolderService.getAllCardHolders(null);
        assertNotNull(list);
        assertThat(list, is(not(empty())));
        assertInstanceOf(List.class, list);
        assertThat(list, everyItem(instanceOf(CardHolderResponse.class)));
    }

    @Test
    void should_return_all_card_holders_wich_status_is_active() {
        when(cardHolderRepository.findByStatus(CardHolderStatus.ACTIVE)).thenReturn(activeCardHolderEntityListFactory());

        List<CardHolderResponse> list = cardHolderService.getAllCardHolders(CardHolderStatus.ACTIVE);
        assertNotNull(list);
        assertThat(list, is(not(empty())));
        assertInstanceOf(List.class, list);
        assertThat(list, everyItem(instanceOf(CardHolderResponse.class)));
//        assertThat(list, everyItem(hasProperty("status", is(equalTo(CardHolderStatus.ACTIVE)))));
    }

    @Test
    void should_return_all_card_holders_wich_status_is_inactive() {
        when(cardHolderRepository.findByStatus(CardHolderStatus.INACTIVE)).thenReturn(inactiveCardHolderEntityListFactory());

        List<CardHolderResponse> list = cardHolderService.getAllCardHolders(CardHolderStatus.INACTIVE);
        assertNotNull(list);
        assertThat(list, is(not(empty())));
        assertInstanceOf(List.class, list);
        assertThat(list, everyItem(instanceOf(CardHolderResponse.class)));
//        assertThat(list, everyItem(hasProperty("status", is(equalTo(CardHolderStatus.INACTIVE)))));
    }

    @Test
    void should_throw_invalid_status_exception_when_guiven_status_is_different_than_active_or_inactive() {
        assertThrows(IllegalArgumentException.class, () -> cardHolderService.getAllCardHolders(
                CardHolderStatus.valueOf("other_than_active_or_inactive")));
    }

    @Test
    void should_return_card_holder_when_getting_card_holder_by_id() {
        when(cardHolderRepository.findById(uuidArgumentCaptor.capture())).thenReturn(Optional.of(activeCardHolderEntityFactory()));

        CardHolderResponse cardHolderResponse = cardHolderService.getCardHolderById(UUID.fromString("c0a8a4d0-8e1f-4b7a-9b1a-8e9a9a9a9a9a"));
        assertInstanceOf(CardHolderResponse.class, cardHolderResponse);
        assertNotNull(cardHolderResponse);
        assertNotNull(cardHolderResponse.id());
        assertNotNull(cardHolderResponse.status());
        assertNotNull(cardHolderResponse.limit());
    }

    @Test
    void should_throw_card_holder_not_found_exception_when_card_holder_not_found_when_getting_card_holder_by_id() {
        when(cardHolderRepository.findById(uuidArgumentCaptor.capture())).thenReturn(Optional.empty());

        assertThrows(CardHolderNotFoundException.class, () -> cardHolderService.getCardHolderById(UUID.fromString("c0a8a4d0-8e1f-4b7a-9b1a-8e9a9a9a9a9a")));
    }
}