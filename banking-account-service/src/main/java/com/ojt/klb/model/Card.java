package com.ojt.klb.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.CreationTimestamp;

import java.math.BigDecimal;
import java.sql.Timestamp;


@Getter
@Setter
@Entity
@Table(name = "cards")
public class Card {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "card_id", nullable = false)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "account_id", nullable = false)
    private Account account;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "card_type_id", nullable = false)
    private CardType cardType;

    @Column(name = "card_number", nullable = false, length = 20)
    private String cardNumber;

    @ColumnDefault("0.00")
    @Column(name = "balance", precision = 15, scale = 2)
    private BigDecimal balance;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private Status status = Status.active;

    public enum Status {
        active,
        inactive,
        closed
    }

    @Column(name = "image_url")
    private String imageUrl;

    @Column(name = "opened_at", nullable = false)
    @CreationTimestamp
    private Timestamp openedAt;

    @Column(name = "closed_at", nullable = false)
    @CreationTimestamp
    private Timestamp closedAt;

}