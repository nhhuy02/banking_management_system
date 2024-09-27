package com.ojt.klb.repository;

import com.ojt.klb.model.CardType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CardTypeRepository extends JpaRepository<CardType, Long> {
    List<CardType> findByIsActiveTrue();

    CardType findByCardTypeName(String cardTypeName);
}
