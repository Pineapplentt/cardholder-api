package com.jazztech.mapper.card;

import com.jazztech.controller.response.card.CardResponse;
import com.jazztech.repository.entity.CardEntity;
import org.mapstruct.Mapper;

@Mapper
public interface CardEntityToResponseMapper {
    CardResponse from(CardEntity cardEntity);
}
