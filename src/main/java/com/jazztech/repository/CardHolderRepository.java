package com.jazztech.repository;

import com.jazztech.repository.entity.CardHolderEntity;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CardHolderRepository extends JpaRepository<CardHolderEntity, UUID> {
}
