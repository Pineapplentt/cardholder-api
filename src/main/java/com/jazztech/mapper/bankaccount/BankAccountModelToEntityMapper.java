package com.jazztech.mapper.bankaccount;

import com.jazztech.model.BankAccountModel;
import com.jazztech.repository.entity.BankAccount;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface BankAccountModelToEntityMapper {
    BankAccount from(BankAccountModel bankAccountEntity);
}
