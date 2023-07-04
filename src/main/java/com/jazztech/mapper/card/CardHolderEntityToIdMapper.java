package com.jazztech.mapper.card;

import com.jazztech.model.CardModel;
import com.jazztech.repository.entity.CardEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper
public interface CardHolderEntityToIdMapper {
    @Mapping(target = "cardHolderId.id", source = "cardHolderId")
    CardEntity from(CardModel cardModel);
}
