package com.jazztech.service;

import static org.junit.jupiter.api.Assertions.*;

import com.jazztech.repository.CardRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class CardServiceTest {

    @Mock
    CardRepository cardRepository;
    @InjectMocks
    CardService cardService;

    @Test
    void should_create_card() {

    }
}