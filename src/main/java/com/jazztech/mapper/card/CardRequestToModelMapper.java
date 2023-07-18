package com.jazztech.mapper.card;

import com.jazztech.controller.request.card.CardRequest;
import com.jazztech.model.CardModel;
import org.mapstruct.Mapper;

@Mapper
public interface CardRequestToModelMapper {
    CardModel from(CardRequest cardRequest);
}
