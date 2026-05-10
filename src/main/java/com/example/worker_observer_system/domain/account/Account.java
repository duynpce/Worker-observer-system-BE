package com.example.worker_observer_system.domain.account;

import com.example.worker_observer_system.domain.account.constant.Role;
import com.example.worker_observer_system.domain.account.constant.Station;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Entity
@Table(name = "accounts")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Account {

    @Id
    @Column(columnDefinition = "uuid", updatable = false, nullable = false)
    private UUID id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(nullable = false)
    private String password;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Station station;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Role role;

    public Account(String name, String email, String password, Station station, Role role) {
        this.id = UUID.randomUUID();
        this.name = name;
        this.email = email;
        this.password = password;
        this.station = station;
        this.role = role;
    }
}
