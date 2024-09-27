package com.ojt.klb.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import org.apache.hc.core5.annotation.Contract;
import org.hibernate.annotations.ColumnDefault;

import java.sql.Timestamp;

@Getter
@Setter
@Entity
@Table(name = "card_registration_requests")
public class CardRegistrationRequest {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "account_id", nullable = false)
    private Account account;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "card_type_id", nullable = false)
    private CardType cardType;

    @Enumerated(EnumType.STRING)
    @Column(name = "request_status", nullable = false)
    private Status requestStatus = Status.pending;

    public enum Status {
        pending,
        approved,
        rejected
    }

    @ColumnDefault("CURRENT_TIMESTAMP")
    @Column(name = "request_date", nullable = false)
    private Timestamp requestDate;

    @Column(name = "review_date")
    private Timestamp reviewDate;

    @Lob
    @Column(name = "notes")
    private String notes;

}