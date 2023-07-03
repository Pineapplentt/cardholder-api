package com.jazztech.mapper.card;

import com.jazztech.controller.response.card.LimitUpdateResponse;
import com.jazztech.repository.entity.CardEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper
public interface CardEntityToLimitUpdateResponseMapper {
    @Mapping(target = "updatedLimit", source = "limit")
    LimitUpdateResponse from(CardEntity cardEntity);
}
