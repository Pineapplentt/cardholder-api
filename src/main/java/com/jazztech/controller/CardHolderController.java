package com.jazztech.controller;

import com.jazztech.controller.request.cardholder.CardHolderRequest;
import com.jazztech.controller.response.cardholder.CardHolderResponse;
import com.jazztech.service.CardHolderService;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/v1.0/card-holder")
@RequiredArgsConstructor
public class CardHolderController {
    private final CardHolderService cardHolderService;

    @PostMapping
    @ResponseStatus(value = HttpStatus.CREATED)
    public CardHolderResponse createCardHolder(@RequestBody CardHolderRequest cardHolderRequest) {
        return this.cardHolderService.createCardHolder(cardHolderRequest);
    }
}
