package com.jazztech.mapper.cardholder;

import com.jazztech.controller.response.cardholder.CardHolderResponse;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface CardHolderEntityToResponseMapper {
    CardHolderResponse from(com.jazztech.repository.entity.CardHolderEntity cardHolderEntity);
}
