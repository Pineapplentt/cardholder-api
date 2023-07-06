package com.jazztech.mapper.card;

import com.jazztech.model.CardModel;
import com.jazztech.repository.entity.CardEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper
public interface CardEntityToModelMapper {

    @Mapping(target = "cardHolderId", source = "cardHolderId.id")
    CardModel from(CardEntity cardEntity);
}
