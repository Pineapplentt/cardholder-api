package com.jazztech.repository;

import com.jazztech.repository.entity.CardHolderEntity;
import com.jazztech.utils.CardHolderStatus;
import jakarta.transaction.Transactional;
import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

public interface CardHolderRepository extends JpaRepository<CardHolderEntity, UUID> {
    List<CardHolderEntity> findByStatus(CardHolderStatus status);

    CardHolderEntity findByClientId(UUID clientId);

    @Transactional
    @Modifying
    @Query("update CardHolderEntity c set c.availableLimit = ?2 where c.id = ?1")
    void updateCardHolderAvailableLimit(UUID id, BigDecimal availableLimit);
}
