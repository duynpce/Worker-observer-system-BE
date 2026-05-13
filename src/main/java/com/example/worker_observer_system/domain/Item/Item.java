package com.example.worker_observer_system.domain.Item;

import com.example.worker_observer_system.domain.account.constant.Station;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.UuidGenerator;

import java.time.Instant;
import java.util.UUID;

@Entity
@Table(name = "items")
@Data
@NoArgsConstructor
public class Item {
    @Id
    @Column(columnDefinition = "uuid", updatable = false, nullable = false)
    @UuidGenerator(style = UuidGenerator.Style.TIME)
    private UUID id;

    @Enumerated(EnumType.STRING)
    @Column(name = "current_station", nullable = false)
    private Station currentStation;

    @Column(name = "created_at", nullable = false)
    private Instant createdAt = Instant.now();
}
