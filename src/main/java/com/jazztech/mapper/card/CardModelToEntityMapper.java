package com.jazztech.mapper.card;

import com.jazztech.model.CardModel;
import com.jazztech.repository.entity.CardEntity;
import com.jazztech.repository.entity.CardHolderEntity;
import java.util.UUID;
import org.mapstruct.Mapper;

@Mapper
public interface CardModelToEntityMapper {
    CardEntity from(CardModel cardModel);

    CardHolderEntity map(UUID cardHolderId);
}
