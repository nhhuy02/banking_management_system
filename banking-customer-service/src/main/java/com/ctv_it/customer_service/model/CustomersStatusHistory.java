package com.ctv_it.customer_service.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.CreationTimestamp;

import java.sql.Timestamp;
import java.time.Instant;

@Setter
@Getter
@Entity
@Table(name = "customers_status_history")
public class CustomersStatusHistory {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "customer_id", nullable = false)
    private Customer customer;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private Status status = Status.active;

    @Column(name = "changed_at", nullable = false, updatable = false)
    @CreationTimestamp
    private Instant changedAt = Instant.now();

    public enum Status {
        active,
        suspended,
        closed
    }

    @PrePersist
    public void prePersist() {
        if (this.changedAt == null) {
            this.changedAt = Instant.now();
        }
    }
}
