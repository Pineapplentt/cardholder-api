package com.jazztech.repository;

import com.jazztech.repository.entity.CardEntity;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CardRepository extends JpaRepository<CardEntity, UUID> {
}
