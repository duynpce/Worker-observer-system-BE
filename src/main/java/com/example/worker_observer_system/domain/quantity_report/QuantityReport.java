package com.example.worker_observer_system.domain.quantity_report;

import com.example.worker_observer_system.domain.account.Account;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;
import java.time.LocalDate;

@Entity
@Table(name = "quantity_reports")
@Data
@NoArgsConstructor
public class QuantityReport {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "quantity")
    private Integer quantity;

    @Column(name = "date", nullable = false )
    private LocalDate date;

    @Column(name = "created_at",nullable = false)
    private Instant createdAt =  Instant.now();

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "account_id", referencedColumnName = "id", nullable = false)
    private Account account;

    public QuantityReport(Integer quantity) {
        this.quantity = quantity;
    }
}
