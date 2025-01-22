package com.jazztech.mapper.cardholder;

import com.jazztech.model.CardHolderModel;
import com.jazztech.repository.entity.CardHolderEntity;
import org.mapstruct.Mapper;

@Mapper
public interface CardHolderEntityToModelMapper {
    CardHolderModel from(CardHolderEntity cardHolderEntity);
}
