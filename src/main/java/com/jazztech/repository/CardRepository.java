package com.jazztech.repository;

import com.jazztech.repository.entity.CardEntity;
import com.jazztech.repository.entity.CardHolderEntity;
import java.util.List;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CardRepository extends JpaRepository<CardEntity, UUID> {

    CardEntity findByCardHolderIdAndCardId(CardHolderEntity cardHolderEntity, UUID cardId);

    List<CardEntity> findByCardHolderId(CardHolderEntity cardHolderEntity);
}
