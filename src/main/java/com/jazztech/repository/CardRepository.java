package com.jazztech.repository;

import com.jazztech.repository.entity.CardEntity;
import com.jazztech.repository.entity.CardHolderEntity;
import jakarta.transaction.Transactional;
import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

public interface CardRepository extends JpaRepository<CardEntity, UUID> {

    Optional<CardEntity> findByCardHolderIdAndCardId(CardHolderEntity cardHolderEntity, UUID cardId);

    List<CardEntity> findByCardHolderId(CardHolderEntity cardHolderEntity);

    @Transactional
    @Query("update CardEntity c set c.limit = ?2 where c.cardId = ?1")
    @Modifying
    void updateCardLimit(UUID cardId, BigDecimal limit);
}
