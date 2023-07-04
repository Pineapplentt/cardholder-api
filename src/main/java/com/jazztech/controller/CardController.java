package com.jazztech.controller;

import com.jazztech.controller.request.card.CardRequest;
import com.jazztech.controller.request.card.LimitUpdateRequest;
import com.jazztech.controller.response.card.CardResponse;
import com.jazztech.controller.response.card.LimitUpdateResponse;
import com.jazztech.service.CardService;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/v1.0/card-holders/{cardHolderId}/cards")
@RequiredArgsConstructor
public class CardController {
    private final CardService cardService;

    @PostMapping
    @ResponseStatus(value = HttpStatus.CREATED)
    public CardResponse createCard(@PathVariable UUID cardHolderId,
                                   @RequestBody CardRequest cardRequest) {
        return this.cardService.createCard(cardHolderId, cardRequest);
    }

    @GetMapping
    public List<CardResponse> getAllCards(@PathVariable UUID cardHolderId) {
        return this.cardService.getAllCards(cardHolderId);
    }

    @GetMapping("/{id}")
    public CardResponse getCardById(@PathVariable UUID cardHolderId,
                                    @PathVariable UUID id) {
        return this.cardService.getCardById(cardHolderId, id);
    }

    @PatchMapping("/{cardId}")
    public LimitUpdateResponse updateCardLimit(@PathVariable UUID cardHolderId,
                                               @PathVariable UUID cardId,
                                               @RequestBody LimitUpdateRequest limitUpdateRequest) {
        return this.cardService.updateCard(cardHolderId, cardId, limitUpdateRequest);
    }
}
