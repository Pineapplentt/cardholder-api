package com.jazztech.mapper.cardholder;

import com.jazztech.controller.request.cardholder.CardHolderRequest;
import com.jazztech.model.CardHolderModel;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface CardHolderRequestToModelMapper {
    CardHolderModel from(CardHolderRequest cardHolderRequest);
}
